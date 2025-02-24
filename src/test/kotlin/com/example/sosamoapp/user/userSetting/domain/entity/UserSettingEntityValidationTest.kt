package com.example.sosamoapp.user.userSetting.domain.entity

import com.example.sosamoapp.userSetting.domain.ThemePreference
import com.example.sosamoapp.userSetting.domain.UserSettingEnabled
import com.example.sosamoapp.userSetting.domain.entity.UserSettingEntity
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserSettingEntityValidationTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `유효한 UserSettingEntity는 유효성을 통과`() {
        val entity = UserSettingEntity().apply {
            userSeq = 100
            notificationEnabled = UserSettingEnabled.ON
            eventEnabled = UserSettingEnabled.ON
            themePreference = ThemePreference.DARK
        }

        val violations: Set<ConstraintViolation<UserSettingEntity>> = validator.validate(entity)
        assertEquals(0, violations.size, "유효한 엔티티는 유효성 검사를 통과해야 합니다.")
    }

    @Test
    fun `userSeq가 양수면 유효성 통과`() {
        val entity = UserSettingEntity().apply {
            userSeq = 1
        }

        val violations: Set<ConstraintViolation<UserSettingEntity>> = validator.validate(entity)
        assertEquals(0, violations.size, "userSeq가 양수이면 유효성 검사를 통과해야 합니다.")
    }

    @Test
    fun `userSeq가 0이면 유효성 검사를 통과하지 못함`() {
        val entity = UserSettingEntity().apply {
            userSeq = 0
        }

        val violations: Set<ConstraintViolation<UserSettingEntity>> = validator.validate(entity)
        assertEquals(1, violations.size, "userSeq가 0이면 유효성 검사를 통과하지 못해야 합니다.")

        val violation = violations.first()
        assertEquals("userSeq", violation.propertyPath.toString())
        assertEquals("유저의 시퀸스는 양수여야 합니다.", violation.message)
    }

    @Test
    fun `userSeq가 음수이면 유효성 검사를 통과하지 못함`() {
        val entity = UserSettingEntity().apply {
            userSeq = -5
        }

        val violations: Set<ConstraintViolation<UserSettingEntity>> = validator.validate(entity)
        assertEquals(1, violations.size, "userSeq가 음수이면 유효성 검사를 통과하지 못해야 합니다.")

        val violation = violations.first()
        assertEquals("userSeq", violation.propertyPath.toString())
        assertEquals("유저의 시퀸스는 양수여야 합니다.", violation.message)
    }

    @Test
    fun `여러 필드가 유효하지 않으면 각 제약 조건을 위반한다`() {
        val entity = UserSettingEntity().apply {
            userSeq = -10
            // Enum 필드는 기본값으로 설정되어 있으므로 추가로 설정할 필요 없음
            // 만약 추가적인 제약 조건이 있다면 설정 가능
        }

        val violations: Set<ConstraintViolation<UserSettingEntity>> = validator.validate(entity)
        // userSeq만 제약 조건을 가지고 있으므로 하나의 위반 사항이 예상됨
        assertEquals(1, violations.size, "userSeq가 유효하지 않으면 위반 사항이 하나여야 합니다.")

        val violation = violations.first()
        assertEquals("userSeq", violation.propertyPath.toString())
        assertEquals("유저의 시퀸스는 양수여야 합니다.", violation.message)
    }
}