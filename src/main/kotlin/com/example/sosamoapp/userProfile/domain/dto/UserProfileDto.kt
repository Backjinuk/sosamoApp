package com.example.sosamoapp.userProfile.domain.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive


open class UserProfileDto {

    open var userProfileSeq : Long = 0

    @field:Positive(message = "유저의 시퀸스는 양수여야 합니다.")
    open var userSeq : Long = 0

    // 프로필 사진
    @field:Min(0, message = "파일 시퀸스는 0 이상이여야 합니다.")
    open var fileSeq : Long = 0

    open var introduction : String = ""

    // open val interests : List<String>

}