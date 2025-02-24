package com.example.sosamoapp.user.user.domain.dto

import com.example.sosamoapp.user.domain.dto.UserTokenDto
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.collections.map

class UserTokenDtoValidationTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `유효한 UserTokenDto는 유효성을 통과`() {
        val dto = UserTokenDto().apply {
            userSeq = 100
            refreshToken = "validRefreshToken"
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(0, violations.size, "유효한 DTO는 유효성 검사를 통과해야 합니다.")
    }

    @Test
    fun `userSeq가 양수이면 유효성 통과`() {
        val dto = UserTokenDto().apply {
            userSeq = 1
            refreshToken = "validRefreshToken"
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(0, violations.size, "userSeq가 양수이면 유효성 검사를 통과해야 합니다.")
    }

    @Test
    fun `userSeq가 0이면 유효성 검사를 통과하지 못함`() {
        val dto = UserTokenDto().apply {
            userSeq = -1
            refreshToken = "validRefreshToken"
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(1, violations.size, "userSeq가 0이면 @Min 제약 조건을 위반해야 합니다.")

        val violation = violations.first()
        assertEquals("userSeq", violation.propertyPath.toString())
        assertEquals("유저 시퀸스는 양수여야 합니다.", violation.message)
    }

    @Test
    fun `userSeq가 음수이면 유효성 검사를 통과하지 못함`() {
        val dto = UserTokenDto().apply {
            userSeq = -5
            refreshToken = "validRefreshToken"
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(1, violations.size, "userSeq가 음수이면 @Min 제약 조건을 위반해야 합니다.")

        val violation = violations.first()
        assertEquals("userSeq", violation.propertyPath.toString())
        assertEquals("유저 시퀸스는 양수여야 합니다.", violation.message)
    }

    @Test
    fun `refreshToken이 비어있으면 유효성 검사를 통과하지 못함`() {
        val dto = UserTokenDto().apply {
            userSeq = 100
            refreshToken = ""
            expiredDt = LocalDateTime.now().plusDays(1)
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(1, violations.size, "refreshToken이 비어있으면 @NotBlank 제약 조건을 위반해야 합니다.")

        val violation = violations.first()
        assertEquals("refreshToken", violation.propertyPath.toString())
        assertEquals("refreshToken은 비어있을수 없습니다.", violation.message)
    }

    @Test
    fun `여러 필드가 유효하지 않으면 각 제약 조건을 위반한다`() {
        val dto = UserTokenDto().apply {
            userSeq = -10
            refreshToken = ""
            expiredDt = null
        }

        val violations: Set<ConstraintViolation<UserTokenDto>> = validator.validate(dto)
        assertEquals(2, violations.size, "여러 필드가 유효하지 않으면 각 제약 조건을 위반해야 합니다.")

        val violationProperties = violations.map { it.propertyPath.toString() }.toSet()
        assertTrue(violationProperties.contains("userSeq"), "userSeq 위반 사항이 포함되어야 합니다.")
        assertTrue(violationProperties.contains("refreshToken"), "refreshToken 위반 사항이 포함되어야 합니다.")
    }
}