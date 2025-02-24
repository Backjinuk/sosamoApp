package com.example.sosamoapp.userSetting.infra.repository

import com.example.sosamoapp.userSetting.domain.entity.UserSettingEntity
import org.springframework.stereotype.Repository


@Repository
interface UserSettingRepository {

    fun createDefaultUserSetting(savedEntity: UserSettingEntity) : UserSettingEntity

    fun updateUserSettingByUserSetting(userSettingEntity: UserSettingEntity) : UserSettingEntity

    fun findUserSettingByUserSeq(updateUserSeq: Long) : UserSettingEntity
}