package com.example.sosamoapp.userSetting.domain.dto

import com.example.sosamoapp.userSetting.domain.ThemePreference
import com.example.sosamoapp.userSetting.domain.UserSettingEnabled
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive


open class UserSettingDto {

    open var userSettingSeq: Long? = 0

    @field:NotNull(message = "유저의 시퀸스는 비어있을 수 없슴니다.")
    @field:Positive(message = "유저의 시퀸스는 양수여야 합니다.")
    open var userSeq: Long = 0

    // 알림 설정
    open var notificationEnabled: UserSettingEnabled = UserSettingEnabled.OFF

    // 이벤트 알림 설정
    open var eventEnabled: UserSettingEnabled = UserSettingEnabled.OFF

    // 테마 설정
    open var themePreference: ThemePreference = ThemePreference.LIGTH
}