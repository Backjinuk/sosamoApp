package com.example.sosamoapp.user.user.service

import com.example.sosamoapp.application.service.userService.UserService
import com.example.sosamoapp.domain.repository.UserRepository
import com.example.sosamoapp.domain.enums.UserJoinType
import com.example.sosamoapp.domain.enums.UserRole
import com.example.sosamoapp.domain.dto.user.UserDto
import com.example.sosamoapp.domain.dto.user.UserTokenDto
import com.example.sosamoapp.domain.entity.user.UserEntity
import com.example.sosamoapp.domain.entity.user.UserTokenEntity
import com.example.sosamoapp.util.ValidatorUtil
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.modelmapper.ModelMapper
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class UserServiceUnitTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var modelMapper: ModelMapper

    @MockK
    private lateinit var validatorUtil: ValidatorUtil

    @InjectMockKs
    private lateinit var userService: UserService

    @Nested
    @DisplayName("registerUser 메서드 테스트")
    inner class RegisterUser() {

        @Test
        @DisplayName("성공적인 회원 가입")
        fun `should register user successfully`() {
            // given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            val userEntity = UserEntity().apply {
                email = userDto.email
                passwd = userDto.passwd
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }

            val savedUser = UserEntity().apply {
                userSeq = 1L // assuming userSeq is set upon saving
                email = userDto.email
                passwd = userDto.passwd
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }

            // Mocking Validator to return no violations
            justRun { validatorUtil.validator(userDto) }

            // Mocking ModelMapper to map DTO to Entity
            every { modelMapper.map(userDto, UserEntity::class.java) } returns userEntity

            // Mocking Repository to save the entity
            every { userRepository.userJoin(userEntity) } returns savedUser

            // Mocking ModelMapper to map Entity back to DTO
            val savedUserDto = UserDto().apply {
                userSeq = savedUser.userSeq!!
                email = savedUser.email
                passwd = savedUser.passwd
                nickName = savedUser.nickName
                userRole = savedUser.userRole
                joinType = savedUser.joinType
                // regDt는 필요에 따라 설정
            }
            every { modelMapper.map(savedUser, UserDto::class.java) } returns savedUserDto

            // when
            val result = userService.registerUser(userDto)

            // then
            assertNotNull(result, "결과는 null이 아니어야 합니다.")
            assertEquals(savedUserDto.email, result.email, "email이 일치해야 합니다.")
            assertEquals(savedUserDto.passwd, result.passwd, "passwd가 일치해야 합니다.")
            assertEquals(savedUserDto.nickName, result.nickName, "nickName이 일치해야 합니다.")
            assertEquals(savedUserDto.userRole, result.userRole, "userRole이 일치해야 합니다.")
            assertEquals(savedUserDto.joinType, result.joinType, "joinType이 일치해야 합니다.")

            verify(exactly = 1) { modelMapper.map(userDto, UserEntity::class.java) }
            verify(exactly = 1) { userRepository.userJoin(userEntity) }
            verify(exactly = 1) { modelMapper.map(savedUser, UserDto::class.java) }
        }

        @Test
        @DisplayName("유효하지 않은 데이터로 인해 회원 가입 실패")
        fun `should fail registration when data is invalid`() {
            // given
            val userDto = UserDto().apply {
                // 실제로 유효하지 않은 값들
                email = ""             // 이메일 미기재
                passwd = "1234"       // 비밀번호가 너무 짧음
                nickName = ""          // 닉네임 없음
                userRole = UserRole.User
                joinType = UserJoinType.HOMEPAGE
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(userDto) } throws IllegalArgumentException(
                "유효성 검증 실패: 이메일은 필수 항목입니다., 비밀번호는 8~20자 사이여야 합니다."
            )

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                userService.registerUser(userDto)
            }

            // 예외 메시지 검증
            assertTrue(exception.message?.contains("유효성 검증 실패") == true)
            assertTrue(exception.message?.contains("이메일은 필수 항목입니다.") == true)
            assertTrue(exception.message?.contains("비밀번호는 8~20자 사이여야 합니다.") == true)

            // 검증 메서드가 실제로 호출되었는지 확인
            verify(exactly = 1) { validatorUtil.validator(userDto) }
            // 추가적인 상호작용이 없었는지 확인
            confirmVerified(modelMapper, userRepository, validatorUtil)
        }

        @Test
        @DisplayName("이메일이 이미 존재하는지 확인 - 존재할 경우 true 반환")
        fun `should return true when email exists`() {
            // given
            val userDto = UserDto().apply {
                email = "duplicate@example.com"
                passwd = "SomePass123"
                nickName = "DupUser"
                userRole = UserRole.User
                joinType = UserJoinType.HOMEPAGE
            }

            // repository가 email로 존재하는지 확인하면 true 반환
            every { userRepository.userIsExistsByEmail(userDto.email) } returns true

            // when
            val result = userService.userIsExistsByEmail(userDto)

            // then
            assertTrue(result, "이미 존재하는 이메일이라면 true를 반환해야 합니다.")

            // verify
            verify(exactly = 1) { userRepository.userIsExistsByEmail(userDto.email) }
            confirmVerified(userRepository, modelMapper, validatorUtil)
        }

        @Test
        @DisplayName("이메일 중복으로 인해 회원 가입 실패")
        fun `should fail registration when email is duplicated`() {
            // given
            val userDto = UserDto().apply {
                email = "duplicate@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // repository가 email로 존재하는지 확인하면 true 반환
            every { userRepository.userIsExistsByEmail(userDto.email) } returns true

            // when & then
            val userJoin = userService.userIsExistsByEmail(userDto)

            verify(exactly = 1) { userRepository.userIsExistsByEmail(userDto.email) }

            assertEquals(userJoin, true)
        }

    }

    @Nested
    @DisplayName("updateUserInfoByUser 메서드 테스트")
    inner class UpdateUserInfoByUserTests {

        private val userDto = UserDto().apply {
            email = "valid.email@example.com"
            passwd = "ValidPass123"
            nickName = "ValidNick"
            userRole = UserRole.User
            joinType = UserJoinType.GITHUB
        }

        @Test
        @DisplayName("성공적으로 사용자 정보를 업데이트")
        fun `성공적으로 사용자 정보를 업데이트`() {
            // Given
            val userSeq = 1L
            val existingUserEntity = UserEntity().apply {
                this.userSeq = userSeq
                email = "old.email@example.com"
                passwd = "OldPass123"
                nickName = "OldNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            val updatedUserEntity = UserEntity().apply {
                this.userSeq = userSeq
                email = userDto.email
                passwd = userDto.passwd ?: ""
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }
            val updatedUserDto = UserDto().apply {
                email = userDto.email
                passwd = userDto.passwd
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }

            // Mocking
            every { userRepository.getFindUserInfoByUserSeq(userSeq) } returns existingUserEntity
            every { modelMapper.map(userDto, UserEntity::class.java) } returns updatedUserEntity
            // 유효성 검증은 성공했다고 가정
            justRun { validatorUtil.validator(any<UserDto>()) }
            every { userRepository.updateUserInfoByUser(updatedUserEntity) } returns updatedUserEntity
            every { modelMapper.map(updatedUserEntity, UserDto::class.java) } returns updatedUserDto

            // When
            val result = userService.updateUserInfoByUser(userDto)

            // Then
            assertEquals(userDto.email, result.email)
            assertEquals(userDto.nickName, result.nickName)
            assertEquals(userDto.passwd, result.passwd)
            assertEquals(userDto.userRole, result.userRole)
            assertEquals(userDto.joinType, result.joinType)
        }

        @Test
        @DisplayName("사용자가 존재하지 않아 업데이트 실패")
        fun `사용자가 존재하지 않아 업데이트 실패`() {
            // Given
            val userSeq = 1L
            val userEntity: UserEntity = UserEntity()

            justRun { validatorUtil.validator(userDto) }

            every { modelMapper.map(userDto, UserEntity::class.java) } returns userEntity
            every { userRepository.updateUserInfoByUser(userEntity) } returns userEntity
            every { modelMapper.map(userEntity, UserDto::class.java) } returns userDto

            // When & Then
            val returnValue = userService.updateUserInfoByUser(userDto)

            assertEquals(userEntity.userSeq, 0L)
        }

        @Test
        @DisplayName("유효하지 않은 이메일 형식으로 업데이트 실패")
        fun `유효하지 않은 이메일 형식으로 업데이트 실패`() {
            // Given
            val invalidUserDto = UserDto().apply {
                email = "invalid-email"
                passwd = userDto.passwd
                nickName = userDto.nickName
                userRole = userDto.userRole
                joinType = userDto.joinType
            }

            every { validatorUtil.validator(invalidUserDto) } throws IllegalArgumentException("이메일 형식이 유효하지 않습니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.updateUserInfoByUser(invalidUserDto)
            }
            assertEquals("이메일 형식이 유효하지 않습니다.", exception.message)
        }

    }

    @Nested
    @DisplayName("getFindUserInfoByUserSeq 메서드 테스트")
    inner class GetFindUserInfoByUserSeqTests {

        @Test
        @DisplayName("성공적으로 사용자 정보를 조회")
        fun `성공적으로 사용자 정보를 조회`() {
            // Given
            val userSeq = 1L
            val userEntity = UserEntity().apply {
                this.userSeq = userSeq
                email = "user.email@example.com"
                passwd = "UserPass123"
                nickName = "UserNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            val userDto = UserDto().apply {
                email = userEntity.email
                passwd = userEntity.passwd
                nickName = userEntity.nickName
                userRole = userEntity.userRole
                joinType = userEntity.joinType
            }

            every { userRepository.getFindUserInfoByUserSeq(userSeq) } returns userEntity
            every { modelMapper.map(userEntity, UserDto::class.java) } returns userDto

            // When
            val result = userService.getFindUserInfoByUserSeq(userSeq)

            // Then
            assertEquals(userDto.email, result.email)
            assertEquals(userDto.passwd, result.passwd)
            assertEquals(userDto.nickName, result.nickName)
            assertEquals(userDto.userRole, result.userRole)
            assertEquals(userDto.joinType, result.joinType)

            verify(exactly = 1) { userRepository.getFindUserInfoByUserSeq(userSeq) }
            verify(exactly = 1) { modelMapper.map(userEntity, UserDto::class.java) }
        }

        @Test
        @DisplayName("사용자가 존재하지 않아 조회 실패")
        fun `사용자가 존재하지 않아 조회 실패`() {
            // Given
            val userSeq = 1L
            val userEntity: UserEntity = UserEntity()
            every { userRepository.getFindUserInfoByUserSeq(userSeq) } returns userEntity
            every { modelMapper.map(userEntity, UserDto::class.java) } returns UserDto()
            // When & Then
            val returnValue = userService.getFindUserInfoByUserSeq(userSeq)

            assertEquals(UserEntity().userSeq, returnValue.userSeq)

            verify(exactly = 1) { userRepository.getFindUserInfoByUserSeq(userSeq) }
            verify(exactly = 1) { modelMapper.map(any<UserEntity>(), any<Class<UserDto>>()) }
        }

        @Test
        @DisplayName("유효하지 않은 userSeq로 조회 시도")
        fun `유효하지 않은 userSeq로 조회 시도`() {
            // Given
            val invalidUserSeq = -1L

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.getFindUserInfoByUserSeq(invalidUserSeq)
            }
            assertEquals("유저 시퀸스는 양수여야 합니다.", exception.message)

            verify(exactly = 0) { userRepository.getFindUserInfoByUserSeq(any()) }
            verify(exactly = 0) { modelMapper.map(any<UserEntity>(), any<Class<UserDto>>()) }
        }


        @Test
        @DisplayName("조회된 사용자 정보에 비밀번호가 포함되지 않음")
        fun `조회된 사용자 정보에 비밀번호가 포함되지 않음`() {
            // Given
            val userSeq = 1L
            val userEntity = UserEntity().apply {
                this.userSeq = userSeq
                email = "user.email@example.com"
                passwd = "UserPass123"
                nickName = "UserNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            val userDto = UserDto().apply {
                email = userEntity.email
                passwd = "" // 비밀번호 제거
                nickName = userEntity.nickName
                userRole = userEntity.userRole
                joinType = userEntity.joinType
            }

            every { userRepository.getFindUserInfoByUserSeq(userSeq) } returns userEntity
            every { modelMapper.map(userEntity, UserDto::class.java) } returns userDto

            // When
            val result = userService.getFindUserInfoByUserSeq(userSeq)

            // Then
            assertEquals(userDto.email, result.email)
            assertEquals(userDto.nickName, result.nickName)
            assertEquals(userDto.userRole, result.userRole)
            assertEquals(userDto.joinType, result.joinType)
            assertEquals("", result.passwd)

            verify(exactly = 1) { userRepository.getFindUserInfoByUserSeq(userSeq) }
            verify(exactly = 1) { modelMapper.map(userEntity, UserDto::class.java) }
        }
    }

    @Nested
    @DisplayName("addUserTokenByUserSeq 메서드 테스트")
    inner class AddUserTokenByUserSeq() {

        @Test
        @DisplayName("등록 성공 - 유효한 UserTokenDto는 DB에 저장되고 반환되어야 한다")
        fun `등록 성공 - 유효한 UserTokenDto는 DB에 저장되고 반환되어야 한다`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = 100
                refreshToken = "validRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            val userTokenEntity = UserTokenEntity().apply {
                userSeq = userTokenDto.userSeq
                refreshToken = userTokenDto.refreshToken
                expiredDt = userTokenDto.expiredDt
            }

            val savedEntity = UserTokenEntity().apply {
                userTokenSeq = 1L // assuming userTokenSeq is set upon saving
                userSeq = userTokenDto.userSeq
                refreshToken = userTokenDto.refreshToken
                expiredDt = userTokenDto.expiredDt
                regDt = LocalDateTime.now() // set regDt if needed
            }

            // Mocking Validator to return no violations
            justRun { validatorUtil.validator(userTokenDto) }

            // Mocking ModelMapper to map DTO to Entity
            every { modelMapper.map(userTokenDto, UserTokenEntity::class.java) } returns userTokenEntity

            // Mocking Repository to save the entity
            every { userRepository.addUserTokenByUserSeq(userTokenEntity) } returns savedEntity

            // Mocking ModelMapper to map Entity back to DTO
            val savedUserDto = UserTokenDto().apply {
                userTokenSeq = savedEntity.userTokenSeq
                userSeq = savedEntity.userSeq
                refreshToken = savedEntity.refreshToken
                expiredDt = savedEntity.expiredDt
                // regDt는 필요에 따라 설정
            }
            every { modelMapper.map(savedEntity, UserTokenDto::class.java) } returns savedUserDto

            // When
            val result = userService.addUserTokenByUserSeq(userTokenDto)

            // Then
            assertNotNull(result, "결과는 null이 아니어야 합니다.")
            assertEquals(savedEntity.userTokenSeq, result.userTokenSeq, "userTokenSeq가 일치해야 합니다.")
            assertEquals(userTokenDto.userSeq, result.userSeq, "userSeq가 일치해야 합니다.")
            assertEquals(userTokenDto.refreshToken, result.refreshToken, "refreshToken이 일치해야 합니다.")
            assertEquals(userTokenDto.expiredDt, result.expiredDt, "expiredDt가 일치해야 합니다.")

            verify(exactly = 1) {
                validatorUtil
                    .validator(userTokenDto)
            }
            verify(exactly = 1) { modelMapper.map(userTokenDto, UserTokenEntity::class.java) }
            verify(exactly = 1) { userRepository.addUserTokenByUserSeq(userTokenEntity) }
            verify(exactly = 1) { modelMapper.map(savedEntity, UserTokenDto::class.java) }
            confirmVerified(
                validatorUtil, modelMapper, userRepository
            )
        }

        @Test
        @DisplayName("등록 실패 - userSeq가 음수이면 예외가 발생한다")
        fun `등록 실패 - userSeq가 음수이면 예외가 발생한다`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -1
                refreshToken = "validRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // Mocking ValidatorUtil to throw exception
            every { validatorUtil.validator(userTokenDto) } throws IllegalArgumentException("유저 시퀸스는 양수여야 합니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(exception.message!!.contains("유저 시퀸스는 양수여야 합니다."))

            // verify
            verify(exactly = 1) { validatorUtil.validator(userTokenDto) }
            // No other interactions should occur
            verify { modelMapper wasNot Called }
            verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

            confirmVerified(userRepository, modelMapper, validatorUtil)
        }

        @Test
        @DisplayName("등록 실패 - refreshToken이 비어있으면 예외가 발생한다")
        fun `등록 실패 - refreshToken이 비어있으면 예외가 발생한다`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = 100
                refreshToken = ""
                expiredDt = LocalDateTime.now().plusDays(1)
            }


            every { validatorUtil.validator(userTokenDto) } throws IllegalArgumentException("refreshToken은 비어있을수 없습니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(exception.message!!.contains("refreshToken은 비어있을수 없습니다."))

            // verify
            verify(exactly = 1) { validatorUtil.validator(userTokenDto) }
            // No other interactions should occur
            verify { modelMapper wasNot Called }
            verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

            confirmVerified(
                userRepository, modelMapper, validatorUtil
            )
        }

        @Test
        @DisplayName("등록 실패 - expiredDt가 null이면 예외가 발생한다")
        fun `등록 실패 - expiredDt가 null이면 예외가 발생한다`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = 100
                refreshToken = "validRefreshToken"
                expiredDt = null
            }

            every {
                validatorUtil
                    .validator(userTokenDto)
            } throws IllegalArgumentException("만료시간은 비어있을수 없습니다.")

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(exception.message!!.contains("만료시간은 비어있을수 없습니다."))

            // verify
            verify(exactly = 1) {
                validatorUtil
                    .validator(userTokenDto)
            }
            // No other interactions should occur
            verify { modelMapper wasNot Called }
            verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

            confirmVerified(
                userRepository, modelMapper, validatorUtil
            )
        }

        @Test
        @DisplayName("등록 실패 - 모든 필드가 유효하지 않으면 여러 예외가 발생한다")
        fun `등록 실패 - 모든 필드가 유효하지 않으면 여러 예외가 발생한다`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -10
                refreshToken = ""
                expiredDt = null
            }

            every { validatorUtil.validator(userTokenDto) } throws IllegalArgumentException(
                "유효성 검증 실패: 유저 시퀸스는 양수여야 합니다., refreshToken은 비어있을수 없습니다., 만료시간은 비어있을수 없습니다."
            )

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(exception.message!!.contains("유저 시퀸스는 양수여야 합니다."))
            assertTrue(exception.message!!.contains("refreshToken은 비어있을수 없습니다."))
            assertTrue(exception.message!!.contains("만료시간은 비어있을수 없습니다."))

            // verify
            verify(exactly = 1) { validatorUtil.validator(userTokenDto) }

            // No other interactions should occur
            verify { modelMapper wasNot Called }
            verify { userRepository.addUserTokenByUserSeq(any()) wasNot Called }

            confirmVerified(
                userRepository, modelMapper, validatorUtil
            )
        }
    }

    @Nested
    @DisplayName("getFindUserInfoByEmailAndPassword 메서드 테스트")
    inner class GetFindUserInfoByEmailAndPasswordTests {

        @Test
        @DisplayName("성공적으로 사용자 정보를 조회")
        fun `성공적으로 사용자 정보를 조회`() {
            // Given
            var email = "valid.email@example.com"
            var passwd = "ValidPass123"

            val userEntity = UserEntity().apply {
                email = email
                passwd = passwd
                nickName = "UserNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            val userDto = UserDto().apply {
                email = userEntity.email
                passwd = userEntity.passwd
                nickName = userEntity.nickName
                userRole = userEntity.userRole
                joinType = userEntity.joinType
            }

            every { userRepository.getFindUserInfoByEmailAndPasswd(email, passwd) } returns userEntity

            every { modelMapper.map(userEntity, UserDto::class.java) } returns userDto

            // When
            val result = userService.getFindUserInfoByEmailAndPassword(email, passwd)

            // Then
            assertEquals(userDto.email, result.email)
            assertEquals(userDto.passwd, result.passwd)
            assertEquals(userDto.nickName, result.nickName)
            assertEquals(userDto.userRole, result.userRole)
            assertEquals(userDto.joinType, result.joinType)

            verify(exactly = 1) { userRepository.getFindUserInfoByEmailAndPasswd(email, passwd) }
            verify(exactly = 1) { modelMapper.map(userEntity, UserDto::class.java) }

        }

        @Test
        fun `사용자가 존재하지 않아 조회 실패`() {
            // Given
            var email = "valid.email@example.com"
            var passwd = "ValidPass123"

            every {
                userRepository.getFindUserInfoByEmailAndPasswd(
                    email,
                    passwd
                )
            } throws IllegalArgumentException("존재하지 않는 사용자 입니다.")

            val exception =
                assertThrows<IllegalArgumentException> { userService.getFindUserInfoByEmailAndPassword(email, passwd) }

            assertTrue(exception.message!!.contains("존재하지 않는 사용자 입니다."))

        }
    }



}



