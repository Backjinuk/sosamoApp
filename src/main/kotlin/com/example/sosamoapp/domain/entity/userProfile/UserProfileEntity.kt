package com.example.sosamoapp.domain.entity.userProfile

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive


@Entity
open class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_seq")
    @SequenceGenerator(name = "user_profile_seq", sequenceName = "user_profile_seq", allocationSize = 1)
    open var userProfileSeq: Long ?= 0  // Nullable로 변경

    @field:Positive(message = "유저의 시퀸스는 양수여야 합니다.")
    open var userSeq: Long = 0

    // 프로필 사진
    @field:Min(0, message = "파일 시퀸스는 0 이상이어야 합니다.")
    open var fileSeq: Long = 0

    open var introduction: String = ""
}