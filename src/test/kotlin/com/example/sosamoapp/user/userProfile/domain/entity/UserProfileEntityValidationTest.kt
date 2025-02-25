package com.example.sosamoapp.user.userProfile.domain.entity

import com.example.sosamoapp.domain.entity.userProfile.UserProfileEntity
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserProfileEntityValidationTest{

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `유효한 UserProfileEntity는 검증에 통과해야 한다`() {
        var userProfile = UserProfileEntity().apply {
            userProfileSeq = 1
            userSeq = 100
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        }

        val violations = validator.validate(userProfile)
        assertEquals(0, violations.size, "유효한 UserProfileEntity는 위반 사항이 없어야 합니다.")
    }


    @Test
    fun `유효한하지 않은 UserProfileEntity는 검증에 통과하지 못한다`() {
        var userProfile = UserProfileEntity().apply {
            userProfileSeq = 1
            userSeq = -1
            fileSeq = 200
            introduction = "안녕하세요, 저는 사용자입니다."
        }

        val violations = validator.validate(userProfile)

        assertEquals(1, violations.size, "유효한 UserProfileEntity는 위반 사항이 없어야 합니다.")
        assertEquals("유저의 시퀸스는 양수여야 합니다.",  violations.iterator().next().message)
    }

    @Test
    fun `introduction 필드는 빈 문자열이어도 검증에 통과해야 한다`() {
        val userProfile = UserProfileEntity().apply {
            userSeq = 100
            introduction = ""
        }

        val violations = validator.validate(userProfile)
        assertEquals(0, violations.size, "introduction 필드는 빈 문자열이어도 위반 사항이 없어야 합니다.")
    }

}