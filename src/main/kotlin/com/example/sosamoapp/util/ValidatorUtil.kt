package com.example.sosamoapp.util

import jakarta.validation.Validator
import org.springframework.stereotype.Component

@Component
class ValidatorUtil(
    private val validator: Validator,
) {

    fun validator( any: Any ){
        val violations = validator.validate(any) // 변수명 변경

        if (violations.isNotEmpty()) {
            throw IllegalArgumentException(
                "유효성 검증 실패: " + violations.joinToString { it.message }
            )
        }
    }
}