package com.example.sosamoapp.user.userSetting.service

import ValidatorUtil
import com.example.sosamoapp.userSetting.service.UserSettingService
import com.example.sosamoapp.userSetting.domain.ThemePreference
import com.example.sosamoapp.userSetting.domain.UserSettingEnabled
import com.example.sosamoapp.userSetting.domain.dto.UserSettingDto
import com.example.sosamoapp.userSetting.domain.entity.UserSettingEntity
import com.example.sosamoapp.userSetting.infra.repository.UserSettingRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UserProfileServiceUnitTest {

    @MockK
    private lateinit var userSettingRepository: UserSettingRepository
    @MockK
    private lateinit var modelMapper: ModelMapper

    @MockK
    private lateinit var validatorUtil: ValidatorUtil

    @InjectMockKs
    private lateinit var userSettingService: UserSettingService


    @Nested
    @DisplayName("createDefaultUserSetting 메서드 테스트")
    inner class createDefaultUserSettingTests {

        @Test
        fun `등록 성공 - 유효한 userSettingDto는 db에 저장된다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            val userSettingEntity = UserSettingEntity().apply {
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            justRun { validatorUtil.validator(userSettingDto) }
            every {userSettingRepository.createDefaultUserSetting(userSettingEntity)} returns userSettingEntity

            every {modelMapper.map(userSettingDto, UserSettingEntity::class.java)} returns userSettingEntity
            every {modelMapper.map(userSettingEntity, UserSettingDto::class.java)} returns userSettingDto

           //When
            val result = userSettingService.createDefaultUserSettings(userSettingDto)

            //Then
            assertThat(result).isEqualTo(userSettingDto)
        }

        @Test
        fun `등록 실패 - 유효하지 않은 userSettingDto는 db에 저장되지 않는다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSeq = -1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            every { validatorUtil.validator(userSettingDto) } throws IllegalArgumentException("유저의 시퀸스는 양수여야 합니다.")

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.createDefaultUserSettings(userSettingDto)
            }

            assertTrue { exception.message!!.contains("유저의 시퀸스는 양수여야 합니다.") }
        }
    }

    @Nested
    @DisplayName("updateUserSettingByUserSetting 메서드 테스트")
    inner class updateUserSettingByUserSettingTests {

        @Test
        fun `수정 성공 - 유효한 userSettingDto는 db에 수정된다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSettingSeq = 1
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            val userSettingEntity = UserSettingEntity().apply {
                userSettingSeq = 1
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            justRun { validatorUtil.validator(userSettingDto) }
            every {userSettingRepository.updateUserSettingByUserSetting(userSettingEntity)} returns userSettingEntity

            every {modelMapper.map(userSettingDto, UserSettingEntity::class.java)} returns userSettingEntity
            every {modelMapper.map(userSettingEntity, UserSettingDto::class.java)} returns userSettingDto

           //When
            val result = userSettingService.updateUserSettingByUserSetting(userSettingDto)

            //Then
            assertThat(result).isEqualTo(userSettingDto)
        }

        @Test
        fun `수정 실패 - 유효하지 않은 userSettingDto는 db에 수정되지 않는다`(){
            //Given
            val userSettingDto = UserSettingDto().apply {
                userSettingSeq = 1
                userSeq = -1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            every { validatorUtil.validator(userSettingDto) } throws IllegalArgumentException("유저의 시퀸스는 양수여야 합니다.")

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.updateUserSettingByUserSetting(userSettingDto)
            }

            assertTrue { exception.message!!.contains("유저의 시퀸스는 양수여야 합니다.") }
        }

        @Test
        fun `수정 실패 - userSetting가 존재하지 않으면 경우 db에 수정되지 않는다`(){

            //Given
            val userSettingDto = UserSettingDto().apply {
                userSettingSeq = 1
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            justRun { validatorUtil.validator(userSettingDto) }
            every { userSettingRepository.updateUserSettingByUserSetting(any()) } throws IllegalArgumentException("존재하지 않는 유저 입니다.")
            every { modelMapper.map(userSettingDto, UserSettingEntity::class.java) } returns UserSettingEntity()
            every { modelMapper.map(any(), UserSettingDto::class.java) } returns userSettingDto

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.updateUserSettingByUserSetting(userSettingDto)
            }

            assertTrue { exception.message!!.contains("존재하지 않는 유저 입니다.") }
        }
    }


    @Nested
    @DisplayName("findUserSettingInfoByUserSeq 메서드 테스트")
    inner class findUserSettingInfoByUserSeqTests {

        @Test
        fun `조회 성공 - userSeq에 해당하는 userSettingDto를 반환한다`(){
            //Given
            val updateUserSeq = 1L

            val userSettingDto = UserSettingDto().apply {
                userSettingSeq = 1
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            val userSettingEntity = UserSettingEntity().apply {
                userSettingSeq = 1
                userSeq = 1
                notificationEnabled = UserSettingEnabled.OFF
                eventEnabled = UserSettingEnabled.OFF
                themePreference = ThemePreference.LIGTH
            }

            every { userSettingRepository.findUserSettingByUserSeq(updateUserSeq) } returns userSettingEntity
            every { modelMapper.map(userSettingEntity, UserSettingDto::class.java) } returns userSettingDto

            //When
            val result = userSettingService.findUserSettingInfoByUserSeq(updateUserSeq)

            //Then
            assertThat(result).isEqualTo(userSettingDto)
        }

        @Test
        fun `조회 실패 - userSeq에 해당하는 userSettingDto가 존재하지 않으면 IllegalArgumentException을 반환한다`(){
            //Given
            val userSeq = 1L

            every { userSettingRepository.findUserSettingByUserSeq(userSeq) } throws IllegalArgumentException("존재하지 않는 유저 입니다.")

            //When
            val exception = assertThrows<IllegalArgumentException> {
                userSettingService.findUserSettingInfoByUserSeq(userSeq)
            }

            assertTrue { exception.message!!.contains("존재하지 않는 유저 입니다.") }
        }
    }


}
