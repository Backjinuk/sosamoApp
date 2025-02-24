package com.example.sosamoapp.user.userProfile.service

import com.example.sosamoapp.userProfile.domain.dto.SocialMediaPlatFormDto
import com.example.sosamoapp.userProfile.domain.dto.UserProfileDto
import com.example.sosamoapp.userProfile.infra.repository.UserProfileRepository
import com.example.sosamoapp.userProfile.service.UserProfileService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserProfileServiceIntegrationTest @Autowired constructor(
    private val userProfileService: UserProfileService
) {

    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    @Nested
    @DisplayName("createDefaultUserProfile 메서드 테스트")
    inner class CreateDefaultUserProfileTests {

        @Test
        fun `등록 성공 - 유효한 userProfileDto의 값은 db에 등록됨`() {
            //given
            val userProfileDto = UserProfileDto().apply {
                userSeq = 100
                fileSeq = 200
                introduction = "안녕하세요, 저는 사용자입니다."
            } //실제로는 없는 회원

            //when
            val savedUserProfile = userProfileService.createDefaultUserProfile(userProfileDto);

            //than
            assertNotNull(savedUserProfile)
            assertEquals(savedUserProfile.userSeq, userProfileDto.userSeq)
            assertEquals(savedUserProfile.fileSeq, userProfileDto.fileSeq)
            assertEquals(savedUserProfile.introduction, userProfileDto.introduction)
        }

        @Test
        fun `성공적인 등록 - introduction이 빈 값이어도 저장되어야 한다`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userSeq = 100
                fileSeq = 200
                introduction = "" // 빈 값
            }

            // When
            val savedProfile = userProfileService.createDefaultUserProfile(userProfileDto)

            // Then
            assertNotNull(savedProfile)
            assertTrue(savedProfile.userProfileSeq!! > 0)
            assertEquals(userProfileDto.userSeq, savedProfile.userSeq)
            assertEquals(userProfileDto.fileSeq, savedProfile.fileSeq)
            assertEquals(userProfileDto.introduction, savedProfile.introduction)

        }


        @Test
        fun `등록 실패 - 유효하지 않은 UserProfileDto는 저장되지 않아야 한다(userSeq, fileSeq)`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userSeq = 0 // 유효하지 않은 값
                fileSeq = -1 // 유효하지 않은 값
                introduction = "안녕하세요, 저는 사용자입니다."
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userProfileService.createDefaultUserProfile(userProfileDto)
            }

            // 예외 메시지 출력 (디버깅 용도)
            println("Exception message: ${exception.message}")

            // 예외 메시지에 두 개의 검증 실패 메시지가 포함되어 있는지 확인
            assertTrue(exception.message!!.contains("유저의 시퀸스는 양수여야 합니다."))
            assertTrue(exception.message!!.contains("파일 시퀸스는 0 이상이여야 합니다."))

        }
    }

    @Nested
    @DisplayName("socialMediaPlatFromByUserProfile 메서드 테스트")
    inner class SocialMediaPlatFromByUserProfileTests {
        @Test
        fun `등록 성공 - 유효한 socialMediaPlatFormDto는 db에 저장되어야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "https://github.com/testGit"
            }

            // When
            val returnValue = userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

            //Then
            assertEquals(returnValue.userProfileSeq, socialMediaPlatFormDto.userProfileSeq)
            assertEquals(returnValue.platFormName, socialMediaPlatFormDto.platFormName)
            assertEquals(returnValue.platFormUrl, socialMediaPlatFormDto.platFormUrl)

        }


        @Test
        fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = -1
                platFormName = "Github"
                platFormUrl = "https://github.com/testGit"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
        }


        @Test
        fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormName)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = ""
                platFormUrl = "https://github.com/testGit"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
        }


        @Test
        fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = ""
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
        }


        @Test
        fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl - 주소 형식 잘못)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "test-url"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유효한 url이여야 합니다."))
        }


        @Test
        fun `등록 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq, platFormName, platFormUrl)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = -1
                platFormName = ""
                platFormUrl = ""
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
            assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
            assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
        }

    }

    @Nested
    @DisplayName("updateUserProfileInfo 메서드 테스트")
    inner class UpdateUserProfileInfoTests {

        @Test
        fun `성공적인 수정 - 유효한 UserProfileDto는 db에 저장되어야 한다`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userProfileSeq = 100
                userSeq = 200
                fileSeq = 300
                introduction = "안녕하세요, 저는 사용자입니다."
            }

            // When
            val returnValue = userProfileService.updateUserProfileByUserProfile(userProfileDto)

            // Then
            assertEquals(returnValue.userProfileSeq, userProfileDto.userProfileSeq)
            assertEquals(returnValue.userSeq, userProfileDto.userSeq)
            assertEquals(returnValue.fileSeq, userProfileDto.fileSeq)
            assertEquals(returnValue.introduction, userProfileDto.introduction)
        }

        @Test
        fun `수정 실패 - 유효하지 않은 UserProfileDto(userProfileSeq)는 db에 저장되지 않아야 한다`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userProfileSeq = -1
                userSeq = 200
                fileSeq = 300
                introduction = "안녕하세요, 저는 사용자입니다."
            }

            // When && Then
            val exceptions = assertThrows<InvalidDataAccessApiUsageException> {
                userProfileService.updateUserProfileByUserProfile(userProfileDto)
            }

            assertTrue(exceptions.message!!.contains("존재하지 않는 사용자입니다."))
        }

        @Test
        fun `수정 실패 - 유효하지 않은 UserProfileDto(userSeq)는 db에 저장되지 않아야 한다`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userProfileSeq = 100
                userSeq = -1
                fileSeq = 300
                introduction = "안녕하세요, 저는 사용자입니다."
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateUserProfileByUserProfile(userProfileDto)
            }

            assertTrue(exceptions.message!!.contains("유저의 시퀸스는 양수여야 합니다."))
        }

    }

    @Nested
    @DisplayName("findUserProfileInfoByUserSeq 메서드 테스트")
    inner class FindUserProfileInfoByUserSeqTests {

        @Test
        fun `성공적인 조회 - 유효한 userSeq로 조회한 결과는 null이 아니어야 한다`() {
            // Given
            val userProfileDto = UserProfileDto().apply {
                userSeq = 100
                fileSeq = 200
                introduction = "안녕하세요, 저는 사용자입니다."
            } //실제로는 없는 회원

            val savedUserProfile = userProfileService.createDefaultUserProfile(userProfileDto);

            // When
            val returnValue = userProfileService.findUserProfileInfoByUserSeq(savedUserProfile!!.userSeq)

            // Then
            assertNotNull(returnValue)
            assertEquals(returnValue.userSeq, savedUserProfile.userSeq)
        }

        @Test
        fun `조회 실패 - 유효하지 않은 userSeq로 조회한 결과는 null이어야 한다`() {
            // Given
            val userSeq = -1L

            // When
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userProfileService.findUserProfileInfoByUserSeq(userSeq)
            }

            // Then
            assertTrue(exception.message!!.contains("존재하지 않는 사용자입니다."))
        }

    }

    @Nested
    @DisplayName("updateSocialMediaPlatFormByUserProfile 메서드 테스트")
    inner class UpdateSocialMediaPlatFormByUserProfileTests {

        @Test
        fun `수정 성공 - 유효한 socialMediaPlatFormDto는 db에 저장 되어야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "https://github.com/testGit"
            }

            val returnValue = userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

            val updateSocialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                socialMediaPlatFormSeq = returnValue.socialMediaPlatFormSeq
                userProfileSeq = 100
                platFormName = "Facebook"
                platFormUrl = "https://facebook.com/testFacebook"
            }

            val socialMediaPlatFromByUserProfile =
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(updateSocialMediaPlatFormDto)

            assertNotNull(socialMediaPlatFromByUserProfile)
            assertEquals(socialMediaPlatFromByUserProfile.userProfileSeq, updateSocialMediaPlatFormDto.userProfileSeq)
            assertEquals(socialMediaPlatFromByUserProfile.platFormName, updateSocialMediaPlatFormDto.platFormName)
            assertEquals(socialMediaPlatFromByUserProfile.platFormUrl, updateSocialMediaPlatFormDto.platFormUrl)
        }

        @Test
        fun `수정 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = -1
                platFormName = "Github"
                platFormUrl = "https://github.com"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
        }

        @Test
        fun `수정 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormName)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = ""
                platFormUrl = "https://github.com"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
        }

        @Test
        fun `수정 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = ""
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
        }

        @Test
        fun `수정 실패 - 유효하지 않은 socialMediaPlatFormDto(platFormUrl - 주소 형식 잘못)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "test-url"
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유효한 url이여야 합니다."))
        }

        @Test
        fun `수정 실패 - 유효하지 않은 socialMediaPlatFormDto(userProfileSeq, platFormName, platFormUrl)는 db에 저장되지 않아야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = -1
                platFormName = ""
                platFormUrl = ""
            }

            // When && Then
            val exceptions = assertThrows<IllegalArgumentException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(socialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("유저 프로필의 시퀸스는 0이상이여야 합니다."))
            assertTrue(exceptions.message!!.contains("플랫폼 이름은 비어있을수 없습니다."))
            assertTrue(exceptions.message!!.contains("url은 비어있을수 없습니다."))
        }

        @Test
        fun `수정 실패 - 존재하지 않는 socialMediaPlatSeq로 수정하려고 하면 예외가 발생해야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "https://github.com"
            }

            val socialMediaPlatFormByUserProfile =
                userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)

            val updateSocialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Facebook"
                platFormUrl = "https://facebook.com"
                socialMediaPlatFormSeq = -1
            }

            // When && Then
            val exceptions = assertThrows<InvalidDataAccessApiUsageException> {
                userProfileService.updateSocialMediaPlatFormBySocialMediaPlatForm(updateSocialMediaPlatFormDto)
            }

            assertTrue(exceptions.message!!.contains("존재하지 않는 사용자입니다."))
        }
    }


    @Nested
    @DisplayName("findSocialMediaPlatFormByUserProfileSeq 메서드 테스트")
    inner class FindSocialMediaPlatFormByUserProfileSeqTests {

        @Test
        fun `성공적인 조회 - 유효한 userProfileSeq로 조회한 결과는 null이 아니어야 한다`() {
            // Given
            val socialMediaPlatFormDto = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Github"
                platFormUrl = "https://github.com/testGit"
            }


            val socialMediaPlatFormDto2 = SocialMediaPlatFormDto().apply {
                userProfileSeq = 100
                platFormName = "Facebook"
                platFormUrl = "https://facebook.com/testFacebook"
            }


            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto)
            userProfileService.socialMediaPlatFromByUserProfile(socialMediaPlatFormDto2)

            // When
            val returnValue = userProfileService.findSocialMediaPlatFormByUserProfileSeq(socialMediaPlatFormDto.userProfileSeq)

            // Then
            assertNotNull(returnValue)
            assertEquals(returnValue.size, 2)
        }

        @Test
        fun `조회 실패 - 유효하지 않은 userProfileSeq로 조회한 결과는 null이어야 한다`() {
            // Given
            val userProfileSeq = 100L

            // When
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userProfileService.findSocialMediaPlatFormByUserProfileSeq(userProfileSeq)
            }

            // Then
            assertTrue(exception.message!!.contains("존재하지 않는 사용자입니다."))
        }
    }
}
