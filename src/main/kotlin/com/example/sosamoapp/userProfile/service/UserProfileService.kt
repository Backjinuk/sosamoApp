package com.example.sosamoapp.userProfile.service

import com.example.sosamoapp.userProfile.domain.dto.SocialMediaPlatFormDto
import com.example.sosamoapp.userProfile.domain.dto.UserProfileDto
import com.example.sosamoapp.userProfile.domain.entity.SocialMediaPlatFormEntity
import com.example.sosamoapp.userProfile.domain.entity.UserProfileEntity
import com.example.sosamoapp.userProfile.infra.repository.UserProfileRepository
import com.example.sosamoapp.util.ValidatorUtil
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service


@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val modelMapper: ModelMapper,
    private val validatorUtil: ValidatorUtil
) {
    fun createDefaultUserProfile(userProfileDto: UserProfileDto): UserProfileDto? {

        validatorUtil.validator(userProfileDto)

        val returnValue =
            userProfileRepository.userProfitableSetting(modelMapper.map(userProfileDto, UserProfileEntity::class.java))
        return modelMapper.map(returnValue, UserProfileDto::class.java)
    }

    fun socialMediaPlatFromByUserProfile(socialMediaPlatFormDto: SocialMediaPlatFormDto): SocialMediaPlatFormDto {
        validatorUtil.validator(socialMediaPlatFormDto)

        val returnValue = userProfileRepository.socialMediaPlatFromByUserProfile( modelMapper.map( socialMediaPlatFormDto, SocialMediaPlatFormEntity::class.java ) )
        return modelMapper.map(returnValue, SocialMediaPlatFormDto::class.java)
    }

    fun updateUserProfileByUserProfile(userProfileDto: UserProfileDto): UserProfileDto {
        validatorUtil.validator(userProfileDto)

        val returnValue =
            userProfileRepository.updateUserProfileByUserProfile(modelMapper.map(userProfileDto, UserProfileEntity::class.java))
        return modelMapper.map(returnValue, UserProfileDto::class.java)
    }

    fun findUserProfileInfoByUserSeq(userSeq : Long): UserProfileDto {
        val returnValue = userProfileRepository.findUserProfileByUserSeq(userSeq)
        return modelMapper.map(returnValue, UserProfileDto::class.java)

    }

    fun updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto: SocialMediaPlatFormDto): SocialMediaPlatFormDto {
        validatorUtil.validator(socialMediaPlatFormDto)

        val returnValue = userProfileRepository.updateSocialMediaPlatFormBySocialMediaForm(
            modelMapper.map(
                socialMediaPlatFormDto,
                SocialMediaPlatFormEntity::class.java
            )
        )
        return modelMapper.map(returnValue, SocialMediaPlatFormDto::class.java)
    }

    fun findSocialMediaPlatFormByUserProfileSeq(userProfileSeq: Long): List<SocialMediaPlatFormDto> {
        return userProfileRepository.findSocialMediaPlatFormByUserProfileSeq(userProfileSeq)
            .map { modelMapper.map(it, SocialMediaPlatFormDto::class.java) }
    }
}