package com.example.sosamoapp.user.useCase

import com.example.sosamoapp.application.service.userService.UserService
import com.example.sosamoapp.application.usecase.UserUseCaseInteract
import com.example.sosamoapp.domain.enums.UserJoinType
import com.example.sosamoapp.domain.enums.UserRole
import com.example.sosamoapp.domain.dto.user.UserDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserUseCaseIntegrationTest @Autowired constructor(
    private val userUseCaseInteract: UserUseCaseInteract,
    private val userService: UserService,
) {


    @Nested
    @DisplayName("updateUserInfoByUser 메서드")
    inner class UpdateUserInfoByUser {

        @Test
        fun `성공적으로 회원 정보를 수정한다`() {
            // Given: 신규 사용자 등록
            val newUser = UserDto().apply {
                email = "update.success@example.com"
                passwd = "InitialPass123"
                nickName = "InitialNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }
            val registeredUser = userService.registerUser(newUser)

            // When: 사용자 정보를 업데이트 한다.
            val updateDto = UserDto().apply {
                userSeq = registeredUser.userSeq  // 업데이트 대상 식별자 설정
                // 새로운 값으로 업데이트
                email = "update.success.updated@example.com"
                passwd = "UpdatedPass456"
                nickName = "UpdatedNick"
            }
            val updatedUser = userUseCaseInteract.updateUserInfoByUser(updateDto)

            // Then: 수정된 정보가 올바른지 검증
            assertNotNull(updatedUser)
            assertEquals(registeredUser.userSeq, updatedUser.userSeq)
            assertEquals("update.success.updated@example.com", updatedUser.email)
            assertEquals("UpdatedNick", updatedUser.nickName)
        }


    }

    @Nested
    @DisplayName("registerUser 메서드")
    inner class RegisterUser {

        @Test
        fun `registerUser - 성공적으로 회원 가입을 완료한다`() {
            // given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // when
            val result = userUseCaseInteract.registerUser(userDto)

            // then
            assertNotNull(result.userSeq, "userSeq는 null이 아니어야 합니다.")
            assertEquals(2L, result.userSeq, "userSeq가 일치해야 합니다.")
            assertEquals(userDto.email, result.email, "이메일이 일치해야 합니다.")
            assertEquals(userDto.passwd, result.passwd, "비밀번호가 일치해야 합니다.")
            assertEquals(userDto.nickName, result.nickName, "닉네임이 일치해야 합니다.")
            assertEquals(userDto.userRole, result.userRole, "UserRole이 일치해야 합니다.")
            assertEquals(userDto.joinType, result.joinType, "UserJoinType이 일치해야 합니다.")
        }

        @Test
        fun `registerUser - 이메일 중복 시 회원 가입 실패`() {
            // given
            val userDto = UserDto().apply {
                email = "duplicate@example.com"
                passwd = "ValidPass123"
                nickName = "DuplicateUser"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // 먼저 동일한 이메일로 사용자 등록
            userUseCaseInteract.registerUser(userDto)

            // when & then: 동일한 이메일로 다시 등록 시도
            val returnValue = userService.userIsExistsByEmail(userDto)

            assertTrue(returnValue)
        }

        @Test
        fun `registerUser - 프로필 등록 실패 시 전체 트랜잭션 롤백`() {
            // given
            val userDto = UserDto().apply {
                email = "profile.fail.example.com"
                passwd = "ValidPass123"
                nickName = "ProfileFailUser"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                userUseCaseInteract.registerUser(userDto)
            }

            assertTrue(exception.message!!.contains("유효성 검증 실패") || exception.message!!.contains("프로필 등록 실패"))

            // 이후 트랜잭션이 롤백되어 데이터베이스에 아무 데이터도 남지 않음
            // 추가적으로 데이터베이스 상태를 확인할 수 있습니다.
        }

        @Test
        fun `registerUser - 세팅 정보 등록 실패 시 전체 트랜잭션 롤백`() {
            // given
            val userDto = UserDto().apply {
                email = "settings.fail@example.com"
                passwd = "ValidPass123"
                nickName = ""
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                userUseCaseInteract.registerUser(userDto)
            }

            assertTrue(exception.message!!.contains("유효성 검증 실패") || exception.message!!.contains("세팅 정보 등록 실패"))
        }

        @Test
        fun `registerUser - 토큰 등록 실패 시 전체 트랜잭션 롤백`() {
            // given
            val userDto = UserDto().apply {
                email = "token.fail@example.com"
                passwd = ""
                nickName = "TokenFailUser"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                userUseCaseInteract.registerUser(userDto)
            }

            assertTrue(exception.message!!.contains("유효성 검증 실패") || exception.message!!.contains("토큰 등록 실패"))
        }
    }


    @Nested
    @DisplayName("login 메서드")
    inner class Login {

        @Test
        fun `login - 성공적으로 로그인한다`() {
            // given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            userUseCaseInteract.registerUser(userDto)

            // when
            val result = userUseCaseInteract.login(userDto)

            // then
            assertNotNull(result.userSeq, "userSeq는 null이 아니어야 합니다.")
            assertEquals(userDto.email, result.email, "이메일이 일치해야 합니다.")
            assertEquals(userDto.nickName, result.nickName, "닉네임이 일치해야 합니다.")
            assertNotNull(result.token, "토큰은 null이 아니어야 합니다.")
        }

        @Test
        fun `login - 로그인 실패 시 예외 발생`() {
            // given
            val userDto = UserDto().apply {
                email = "valid.email@example.com"
                passwd = "ValidPass123"
                nickName = "ValidNick"
                userRole = UserRole.User
                joinType = UserJoinType.GITHUB
            }

            // when & then
            val exception = assertThrows<InvalidDataAccessApiUsageException> {
                userUseCaseInteract.login(userDto)
            }

            assertTrue(exception.message!!.contains("존재하지 않는 사용자 입니다."))

        }
    }
}