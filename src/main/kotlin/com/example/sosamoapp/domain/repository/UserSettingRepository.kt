package com.example.sosamoapp.domain.repository

import com.example.sosamoapp.domain.entity.userSetting.UserSettingEntity
import org.springframework.stereotype.Repository


@Repository
interface UserSettingRepository {

    fun createDefaultUserSetting(savedEntity: UserSettingEntity) : UserSettingEntity

    fun updateUserSettingByUserSetting(userSettingEntity: UserSettingEntity) : UserSettingEntity

    fun findUserSettingByUserSeq(updateUserSeq: Long) : UserSettingEntity
}