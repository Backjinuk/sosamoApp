package com.example.sosamoapp.domain.repository

import com.example.sosamoapp.domain.entity.userProfile.SocialMediaPlatFormEntity
import com.example.sosamoapp.domain.entity.userProfile.UserProfileEntity
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