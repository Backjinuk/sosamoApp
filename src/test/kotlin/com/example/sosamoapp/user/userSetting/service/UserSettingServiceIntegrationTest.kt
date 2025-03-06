package com.example.sosamoapp.user.userSetting.service

import com.example.sosamoapp.application.service.userSettingService.UserSettingService
import com.example.sosamoapp.domain.enums.ThemePreference
import com.example.sosamoapp.domain.enums.UserSettingEnabled
import com.example.sosamoapp.domain.dto.userSetting.UserSettingDto
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserSettingServiceIntegrationTest @Autowired constructor(
    private val userSettingService: UserSettingService
) {

    @Nested
    @DisplayName("createDefaultUserSetting 메서드 테스트")
    inner class createDefaultUserSettingTests{

        @Test
        fun `등록 성공 - 유효한 userSettingDto는 db에 저장된다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            //When
            val result = userSettingService.createDefaultUserSettings(userSettingDto)

            //Then
            assertTrue(result.userSettingSeq != 0L)
            assertEquals(result.userSeq, userSettingDto.userSeq)
            assertEquals(result.notificationEnabled, userSettingDto.notificationEnabled)
            assertEquals(result.eventEnabled, userSettingDto.eventEnabled)
            assertEquals(result.themePreference, userSettingDto.themePreference)
        }

        @Test
        fun `등록 실패 - userSeq가 0인 경우 예외가 발생한다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = -1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.createDefaultUserSettings(userSettingDto)
            }

            //Then
            assertTrue { exception.message!!.contains("유저의 시퀸스는 양수여야 합니다.") }
        }

    }

    @Nested
    @DisplayName("updateUserSetting 메서드 테스트")
    inner class updateUserSettingByUserSettingTests {

        @Test
            fun `수정 성공 - 유효한 userSettingDto는 db에 수정된다`(){
                //Given
                val userSettingDto = UserSettingDto().apply {
                    userSeq = 1
                    notificationEnabled = UserSettingEnabled.ON
                    eventEnabled = UserSettingEnabled.ON
                    themePreference = ThemePreference.DARK
                }

            val createDefaultUserSettings = userSettingService.createDefaultUserSettings(userSettingDto)

            //When
                val result = userSettingService.updateUserSettingByUserSetting(createDefaultUserSettings)

                //Then
                assertEquals(result.userSeq, userSettingDto.userSeq)
                assertEquals(result.notificationEnabled, userSettingDto.notificationEnabled)
                assertEquals(result.eventEnabled, userSettingDto.eventEnabled)
                assertEquals(result.themePreference, userSettingDto.themePreference)
            }

            @Test
            fun `수정 실패 - userSettingSeq가 0인 경우 예외를 발생한다`(){
                //Given
                val userSettingDto = UserSettingDto().apply {
                    userSettingSeq = 0
                    userSeq = 1
                    notificationEnabled = UserSettingEnabled.ON
                    eventEnabled = UserSettingEnabled.ON
                    themePreference = ThemePreference.DARK
                }

                //When
                val exception = assertThrows<InvalidDataAccessApiUsageException> {
                    userSettingService.updateUserSettingByUserSetting(userSettingDto)
                }

                //Then
                assertTrue { exception.message!!.contains("존재하지 않는 사용자입니다.") }

            }

    }


    @Nested
    @DisplayName("findUserSettingInfoByUserSeq 메서드 테스트")
    inner class findUserSettingInfoByUserSeqTests {

        @Test
        fun `조회 성공 - userSeq에 해당하는 UserSettingDto를 반환한다`(){
            //Given

            val userSettingDto = UserSettingDto().apply {
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

             userSettingService.createDefaultUserSettings(userSettingDto)

            val userSeq = 1L

            //When
            val result = userSettingService.findUserSettingInfoByUserSeq(userSeq)

            //Then
            assertEquals(result.userSeq, userSeq)
        }

        @Test
        fun `조회 실패 - userSeq에 해당하는 UserSettingDto가 없는 경우 예외를 발생한다`(){
            //Given
            val userSeq = 0L

            //When
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userSettingService.findUserSettingInfoByUserSeq(userSeq)
            }

            //Then
            assertTrue { exception.message!!.contains("존재하지 않는 사용자입니다.") }
        }

    }

}