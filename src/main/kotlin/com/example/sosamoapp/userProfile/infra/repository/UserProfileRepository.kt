package com.example.sosamoapp.userProfile.infra.repository

import com.example.sosamoapp.userProfile.domain.entity.SocialMediaPlatFormEntity
import com.example.sosamoapp.userProfile.domain.entity.UserProfileEntity
import org.springframework.stereotype.Repository


@Repository
interface UserProfileRepository {

    fun userProfitableSetting(userProfile: UserProfileEntity): UserProfileEntity

    fun updateUserProfileByUserProfile(userProfileEntity: UserProfileEntity) : UserProfileEntity

    fun findUserProfileByUserSeq(userSeq : Long) : UserProfileEntity

    fun socialMediaPlatFromByUserProfile(socialMediaPlatFormEntity: SocialMediaPlatFormEntity): SocialMediaPlatFormEntity

    fun updateSocialMediaPlatFormBySocialMediaForm(socialMediaPlatFormEntity: SocialMediaPlatFormEntity) : SocialMediaPlatFormEntity

    fun findSocialMediaPlatFormByUserProfileSeq(userProfileSeq: Long) : List<SocialMediaPlatFormEntity>


}