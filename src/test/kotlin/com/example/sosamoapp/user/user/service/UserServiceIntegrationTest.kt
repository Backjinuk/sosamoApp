package com.example.sosamoapp.user.user.service

import com.example.sosamoapp.application.service.userService.UserService
import com.example.sosamoapp.domain.enums.UserJoinType
import com.example.sosamoapp.domain.enums.UserRole
import com.example.sosamoapp.domain.dto.user.UserDto
import com.example.sosamoapp.domain.dto.user.UserTokenDto
import com.example.sosamoapp.domain.repository.UserSettingRepository
import jakarta.validation.Validator
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest @Autowired constructor(
    private val userService: UserService,
    private val userSettingRepository: UserSettingRepository,
    private val validator: Validator
) {

    @Nested
    @DisplayName("registerUser 메서드 테스트")
    inner class RegisterUserTests {

        @Test
        @Order(1)
        @DisplayName("회원가입 테스트 - 정상 케이스")
        fun `should register user successfully with valid data`() {
            // Given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // When
            val registeredUser = userService.registerUser(userDto)

            // Then
            assertNotNull(registeredUser.userSeq, "UserSeq should not be null after registration.")
            assertEquals(userDto.email, registeredUser.email, "Emails should match.")
            assertEquals(userDto.passwd, registeredUser.passwd, "Passwords should match.")
            assertEquals(userDto.nickName, registeredUser.nickName, "Nicknames should match.")
            assertEquals(userDto.userRole, registeredUser.userRole, "UserRoles should match.")
            assertEquals(userDto.joinType, registeredUser.joinType, "JoinTypes should match.")
        }

        @Test
        @Order(2)
        @DisplayName("이메일로 사용자 존재 여부 조회 테스트 - 존재하는 경우")
        fun `should return true when user exists by email`() {
            // Given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            userService.registerUser(userDto)

            // When
            val exists = userService.userIsExistsByEmail(userDto)

            // Then
            assertTrue(exists, "User should exist for the given email.")
        }

        @Test
        @Order(3)
        @DisplayName("이메일로 사용자 존재 여부 조회 테스트 - 존재하지 않는 경우")
        fun `should return false when user does not exist by email`() {
            // Given
            val userDto = UserDto().apply {
                email = "nonexistent.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            // When
            val exists = userService.userIsExistsByEmail(userDto)

            // Then
            assertFalse(exists, "User should not exist for the given email.")
        }

        @Test
        @Order(4)
        @DisplayName("회원가입 테스트 - 유효하지 않은 데이터로 인해 실패")
        fun `should throw exception when registering with invalid data`() {
            // Given
            val invalidUserDto = UserDto().apply {
                email = ""                     // 이메일 미기재
                passwd = "1234"               // 비밀번호가 너무 짧음
                nickName = ""                  // 닉네임 없음
                userRole = UserRole.User
                joinType = UserJoinType.HOMEPAGE
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.registerUser(invalidUserDto)
            }

            // 예외 메시지 검증
            assertTrue(
                exception.message?.contains("유효성 검증 실패") == true,
                "Exception message should contain '유효성 검증 실패'."
            )
            assertTrue(
                exception.message?.contains("이메일은 필수 항목입니다.") == true,
                "Exception message should contain '이메일은 필수 항목입니다.'."
            )
            assertTrue(
                exception.message?.contains("비밀번호는 8~20자 사이여야 합니다.") == true,
                "Exception message should contain '비밀번호는 8~20자 사이여야 합니다.'."
            )
            assertTrue(
                exception.message?.contains("닉네임은 필수 항목입니다.") == true,
                "Exception message should contain '닉네임은 필수 항목입니다.'."
            )
        }
    }

    @Nested
    @DisplayName("addUserTokenByUserSeq 메서드 테스트")
    inner class AddUserTokenByUserSeqTests {

        @Test
        @Order(5)
        @DisplayName("토큰 등록 테스트 - 정상 케이스")
        fun `should add user token successfully with valid data`() {
            // Given
            val userDto = UserDto().apply {
                email = "token.user@example.com"
                passwd = "ValidPass123"
                nickName = "TokenUser"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // 사용자 등록
            val registeredUser = userService.registerUser(userDto)

            val userTokenDto = UserTokenDto().apply {
                userSeq = registeredUser.userSeq
                refreshToken = "validRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When
            val savedToken = userService.addUserTokenByUserSeq(userTokenDto)

            // Then
            assertNotNull(savedToken.userTokenSeq, "TokenSeq should not be null after saving.")
            assertEquals(userTokenDto.userSeq, savedToken.userSeq, "userSeq should match.")
            assertEquals(userTokenDto.refreshToken, savedToken.refreshToken, "refreshToken should match.")
            assertEquals(userTokenDto.expiredDt, savedToken.expiredDt, "expiredDt should match.")
        }

        @Test
        @Order(6)
        @DisplayName("토큰 등록 테스트 - userSeq가 음수이면 실패")
        fun `should throw exception when userSeq is negative`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -5
                refreshToken = "invalidRefreshToken"
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("유저 시퀸스는 양수여야 합니다.") == true,
                "Exception message should contain '유저 시퀸스는 양수여야 합니다.'."
            )
        }

        @Test
        @Order(7)
        @DisplayName("토큰 등록 테스트 - refreshToken이 비어있으면 실패")
        fun `should throw exception when refreshToken is empty`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = 100
                refreshToken = ""
                expiredDt = LocalDateTime.now().plusDays(1)
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("refreshToken은 비어있을수 없습니다.") == true,
                "Exception message should contain 'refreshToken은 비어있을수 없습니다.'."
            )
        }

        @Test
        @Order(8)
        @DisplayName("토큰 등록 테스트 - 모든 필드가 유효하지 않으면 여러 예외가 발생")
        fun `should throw multiple exceptions when all fields are invalid`() {
            // Given
            val userTokenDto = UserTokenDto().apply {
                userSeq = -10
                refreshToken = ""
                expiredDt = null
            }

            // When & Then
            val exception = assertThrows<IllegalArgumentException> {
                userService.addUserTokenByUserSeq(userTokenDto)
            }

            assertTrue(
                exception.message?.contains("유저 시퀸스는 양수여야 합니다.") == true,
                "Exception message should contain '유저 시퀸스는 양수여야 합니다.'."
            )
            assertTrue(
                exception.message?.contains("refreshToken은 비어있을수 없습니다.") == true,
                "Exception message should contain 'refreshToken은 비어있을수 없습니다.'."
            )
        }
    }

    @Nested
    @DisplayName("updateUserInfoByUser 메서드 통합 테스트")
    inner class UpdateUserInfoByUserTests {

        @Test
        @DisplayName("성공적으로 사용자 정보를 업데이트")
        fun `성공적으로 사용자 정보를 업데이트`() {
            // Given: 기존 사용자 생성 및 저장
            val userDto = UserDto().apply {
                email = "old.email@example.com"
                passwd = "OldPass123"
                nickName = "OldNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // 실제 DB에 저장 (저장 후 자동 생성된 userSeq 사용)
            val savedUser = userService.registerUser(userDto)

            // 업데이트할 내용 DTO 생성
            val updateDto = UserDto().apply {
                userSeq = savedUser.userSeq
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // When: 업데이트 메서드 호출 (통합 테스트이므로 실제 빈들이 동작)
            val updatedUserDto = userService.updateUserInfoByUser( updateDto)

            // Then: 업데이트된 결과 검증
            assertEquals(updateDto.email, updatedUserDto.email)
            assertEquals(updateDto.nickName, updatedUserDto.nickName)
            assertEquals(updateDto.passwd, updatedUserDto.passwd)
            assertEquals(updateDto.userRole, updatedUserDto.userRole)
            assertEquals(updateDto.joinType, updatedUserDto.joinType)
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 업데이트 시도 시 실패")
        fun `사용자가 존재하지 않아 업데이트 실패`() {
            // Given: DB에 존재하지 않는 userSeq 사용 (예: 9999L)
            val nonExistentUserSeq = 9999L
            val updateDto = UserDto().apply {
                userSeq = nonExistentUserSeq
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // When & Then: 예외 발생 여부 검증
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userService.updateUserInfoByUser(updateDto)
            }

            assertEquals("존재하지 않는 사용자 입니다.", exception.message)
        }

        @Test
        @DisplayName("유효하지 않은 이메일 형식으로 업데이트 실패")
        fun `유효하지 않은 이메일 형식으로 업데이트 실패`() {
            // Given: 기존 사용자 저장
            val userEntity = UserDto().apply {
                email = "old.email@example.com"
                passwd = "OldPass123"
                nickName = "OldNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            val savedUser = userService.registerUser(userEntity)

            // 그리고 이메일 형식이 유효하지 않은 DTO 생성
            val invalidDto = UserDto().apply {
                email = "invalid-email"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // When & Then: Validator에서 예외 발생
            val exception = assertThrows<IllegalArgumentException> {
                userService.updateUserInfoByUser( invalidDto)
            }
            assertTrue(exception.message!!.contains("이메일 형식이 유효하지 않습니다."))
        }

    }

    @Nested
    @DisplayName("getFindUserInfoByUserSeq 메서드 테스트")
    inner class getFindUserInfoByUserSeq {

        @Test
        @DisplayName("존재하는 사용자 정보 조회")
        fun `존재하는 사용자 정보 조회`() {
            // Given: 존재하는 사용자 (예: userSeq = 1L, 테스트 DB에 미리 존재)
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            val registeredUser = userService.registerUser(userDto)

            val userSeq = registeredUser.userSeq

            // When: 조회 실행
            val result = userService.getFindUserInfoByUserSeq(userSeq)

            // Then: 조회된 값이 null이 아니고, 필드들이 올바르게 설정되었는지 검증
            assertNotNull(result)
            // 실제 테스트 환경에 맞게 예상 값에 대한 검증을 추가합니다.
            // 예를 들어, 테스트 DB에 저장된 데이터와 일치하는지 확인합니다.
        }

        @Test
        @DisplayName("존재하지 않는 사용자 정보 조회 시 실패")
        fun `존재하지 않는 사용자 정보 조회 실패`() {
            // Given: DB에 존재하지 않는 userSeq 사용 (예: 9999L)
            val nonExistentUserSeq = 9999L

            // When & Then: 조회 시 IllegalArgumentException 발생 검증
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userService.getFindUserInfoByUserSeq(nonExistentUserSeq)
            }
            assertEquals("존재하지 않는 사용자 입니다.", exception.message)
        }

    }

    @Nested
    @DisplayName("getFindUserInfoByEmailAndPassword 메서드 테스트")
    inner class getFinduserInfoByEmailAndPassword {

        @Test
        fun `이메일과 비밀번호로 사용자 정보 조회`() {
            // Given: 존재하는 사용자 (예: userSeq = 1L, 테스트 DB에 미리 존재)
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            userService.registerUser(userDto)

            // When: 조회 실행
            val result = userService.getFindUserInfoByEmailAndPassword(userDto.email, userDto.passwd)

            // Then: 조회된 값이 null이 아니고, 필드들이 올바르게 설정되었는지 검증
            assertNotNull(result)
        }

       @Test
        fun `존재하지 않는 사용자 정보 조회 시 실패`() {
           // Given: DB에 존재하지 않는 사용자 정보 사용
           val email = "valid.email@example.com"
           val passwd = "ValidPass123"

           val result = assertThrows<InvalidDataAccessApiUsageException> {
               userService.getFindUserInfoByEmailAndPassword(email, passwd)
           }

           assertTrue(result.message!!.contains("존재하지 않는 사용자 입니다."))
       }
    }



}

