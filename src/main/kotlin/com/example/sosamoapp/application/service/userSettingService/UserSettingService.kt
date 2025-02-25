package com.example.sosamoapp.application.service.userSettingService

import com.example.sosamoapp.domain.dto.userSetting.UserSettingDto
import com.example.sosamoapp.domain.entity.userSetting.UserSettingEntity
import com.example.sosamoapp.domain.repository.UserSettingRepository
import com.example.sosamoapp.util.ValidatorUtil
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserSettingService(
    private val userSettingRepository: UserSettingRepository,
    private val modelMapper: ModelMapper,
    private val validatorUtil: ValidatorUtil
) {

    fun createDefaultUserSettings(userSettingDto: UserSettingDto): UserSettingDto {
        validatorUtil.validator(userSettingDto)

        val returnValue = userSettingRepository.createDefaultUserSetting(modelMapper.map(userSettingDto, UserSettingEntity::class.java))

        return modelMapper.map(returnValue, UserSettingDto::class.java)
    }

    fun updateUserSettingByUserSetting(userSettingDto: UserSettingDto): UserSettingDto {
       validatorUtil.validator(userSettingDto)

        val returnValue = userSettingRepository.updateUserSettingByUserSetting(modelMapper.map(userSettingDto, UserSettingEntity::class.java))

        return modelMapper.map(returnValue, UserSettingDto::class.java)
    }

    fun findUserSettingInfoByUserSeq(userSeq: Long): UserSettingDto {
        val returnValue = userSettingRepository.findUserSettingByUserSeq(userSeq)

        return modelMapper.map(returnValue, UserSettingDto::class.java)
    }
}