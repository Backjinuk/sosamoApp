package com.example.sosamoapp.domain.dto.userProfile

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

open class SocialMediaPlatFormDto {

    open var socialMediaPlatFormSeq:Long = 0

    @field:Min(0, message = "유저 프로필의 시퀸스는 0이상이여야 합니다.")
    open var userProfileSeq: Long = 0;

    @field:NotBlank(message = "플랫폼 이름은 비어있을수 없습니다.")
    open var platFormName: String = "";

    @field:NotBlank(message = "url은 비어있을수 없습니다.")
    @field:URL(message = "유효한 url이여야 합니다.")
    open var platFormUrl: String = "";

}