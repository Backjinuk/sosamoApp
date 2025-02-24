package com.example.sosamoapp.user.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

open class UserTokenDto {


    open var userTokenSeq: Long ?= 0

    @field:Positive( message = "유저 시퀸스는 양수여야 합니다.")
    open var userSeq: Long = 0

    @field:NotBlank(message = "refreshToken은 비어있을수 없습니다.")
    open var refreshToken: String = ""

    open var expiredDt: LocalDateTime ?=  null

    open var regDt: LocalDateTime = LocalDateTime.now()

}