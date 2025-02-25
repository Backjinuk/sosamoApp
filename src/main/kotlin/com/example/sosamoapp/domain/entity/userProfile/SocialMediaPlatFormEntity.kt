package com.example.sosamoapp.domain.entity.userProfile

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL


@Entity
open class SocialMediaPlatFormEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_media_platForm_seq")
   @SequenceGenerator(name = "social_media_platForm_seq", sequenceName = "social_media_platForm_seq", allocationSize = 1)
   open var socialMediaPlatFormSeq:Long ?= 0

   @field:Min(0, message = "유저 프로필의 시퀸스는 0 이상이여야 합니다.")
   open var userProfileSeq: Long = 0

   @field:NotBlank(message = "플랫폼 이름은 비어있을수 없습니다.")
   open var platFormName: String = ""

   @field:NotBlank(message = "url은 비어있을수 없습니다.")
   @field:URL(message = "유효한 url이여야 합니다.")
   open var platFormUrl: String = ""
}