package com.example.sosamoapp.domain.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime


@Entity
open class UserTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_token_seq")
    @SequenceGenerator(name = "user_token_seq", sequenceName = "user_token_seq", allocationSize = 1)
    open var userTokenSeq: Long ?= 0

    @field:Min(0, message = "유저 시퀸스는 양수여야 합니다.")
    open var userSeq: Long = 0

    @field:NotBlank(message = "refreshToken은 비어있을수 없습니다.")
    open var refreshToken: String = ""

    open var expiredDt: LocalDateTime ?= null

    open var regDt: LocalDateTime = LocalDateTime.now()
}