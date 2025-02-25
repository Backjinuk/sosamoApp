package com.example.sosamoapp.user.userProfile.domain.entity

import com.example.sosamoapp.domain.entity.userProfile.SocialMediaPlatFormEntity
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.collections.map

class SocialMediaPlatFormEntityValidationTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `유효한 SocialMediaPlatFormEntity는 유효성을 통과`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = 100
            platFormName = "GitHub"
            platFormUrl = "https://github.com/user"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        assertEquals(0, violations.size, "유효한 엔티티는 유효성 검사를 통과해야 합니다.")
    }

    @Test
    fun `유효하지 않은 userProfileSeq면 유효성 통과 실패`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = -1
            platFormName = "GitHub"
            platFormUrl = "https://github.com/user"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        assertEquals(1, violations.size, "userProfileSeq가 유효하지 않으면 위반 사항이 하나여야 합니다.")

        val violation = violations.first()
        assertEquals("userProfileSeq", violation.propertyPath.toString())
        assertEquals("유저 프로필의 시퀸스는 0 이상이여야 합니다.", violation.message)
    }

    @Test
    fun `platFormName이 비어있으면 유효성 통과 실패`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = 1
            platFormName = ""
            platFormUrl = "https://github.com/user"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        assertEquals(1, violations.size, "platFormName이 비어있으면 위반 사항이 하나여야 합니다.")

        val violation = violations.first()
        assertEquals("platFormName", violation.propertyPath.toString())
        assertEquals("플랫폼 이름은 비어있을수 없습니다.", violation.message)
    }

    @Test
    fun `platFormUrl이 비어있으면 @NotBlank 제약 조건을 위반한다`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = 1
            platFormName = "LinkedIn"
            platFormUrl = ""
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        // @NotBlank만 위반되므로 하나의 위반 사항이 발생
        assertEquals(1, violations.size, "platFormUrl이 비어있으면 @NotBlank 제약 조건을 위반해야 합니다.")

        val violationMessages = violations.map { it.message }
        println("violationMessages = ${violationMessages.toString()}")

        assertTrue(
            violationMessages.contains("url은 비어있을수 없습니다."),
            "url은 비어있을수 없습니다. 메시지가 포함되어야 합니다."
        )
        // @URL은 빈 문자열에 대해 위반되지 않으므로 두 번째 검증은 제거
    }

    @Test
    fun `platFormUrl이 유효한 URL 형식이 아니면 @URL 제약 조건을 위반한다`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = 10
            platFormName = "InvalidURLTest"
            platFormUrl = "invalid_url"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        assertEquals(1, violations.size, "platFormUrl이 유효하지 않은 URL 형식일 경우 @URL 제약 조건을 위반해야 합니다.")

        val violation = violations.first()
        assertEquals("platFormUrl", violation.propertyPath.toString())
        assertEquals("유효한 url이여야 합니다.", violation.message)
    }

    @Test
    fun `platFormUrl이 올바른 URL 형식이면 유효성 통과`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = 50
            platFormName = "StackOverflow"
            platFormUrl = "https://stackoverflow.com/users/12345"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        assertEquals(0, violations.size, "올바른 URL 형식일 경우 위반 사항이 없어야 합니다.")
    }

    @Test
    fun `여러 필드가 유효하지 않으면 각 제약 조건을 위반한다`() {
        val entity = SocialMediaPlatFormEntity().apply {
            userProfileSeq = -10
            platFormName = ""
            platFormUrl = "invalid_url"
        }

        val violations: Set<ConstraintViolation<SocialMediaPlatFormEntity>> = validator.validate(entity)
        // userProfileSeq, platFormName, platFormUrl 모두 위반되므로 총 3개
        assertEquals(3, violations.size, "여러 필드가 위반될 경우 각 위반 사항이 모두 포함되어야 합니다.")

        val violationProperties = violations.map { it.propertyPath.toString() }.toSet()
        assertTrue(
            violationProperties.contains("userProfileSeq"),
            "userProfileSeq 위반 사항이 포함되어야 합니다."
        )
        assertTrue(
            violationProperties.contains("platFormName"),
            "platFormName 위반 사항이 포함되어야 합니다."
        )
        assertTrue(
            violationProperties.contains("platFormUrl"),
            "platFormUrl 위반 사항이 포함되어야 합니다."
        )
    }
}