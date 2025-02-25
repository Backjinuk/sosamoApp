package com.example.sosamoapp.application.usecase


import com.example.sosamoapp.application.service.userSettingService.UserSettingService
import com.example.sosamoapp.adapter.web.dto.LoginResponseDto
import com.example.sosamoapp.domain.dto.user.UserDto
import com.example.sosamoapp.domain.dto.user.UserTokenDto
import com.example.sosamoapp.application.service.userService.UserService
import com.example.sosamoapp.domain.dto.userProfile.UserProfileDto
import com.example.sosamoapp.application.service.userProfileService.UserProfileService
import com.example.sosamoapp.domain.dto.userSetting.UserSettingDto
import com.example.sosamoapp.util.JwtUtil
import org.springframework.stereotype.Service


@Service
class UserUseCaseInteract(
    private var userService: UserService,
    private var userSettingService: UserSettingService,
    private var userProfileService: UserProfileService,
    private var jwtUtil: JwtUtil
) : UserUseCase {

    override fun registerUser(userDto: UserDto): UserDto {
        // 1. 회원 정보 등록
        val registeredUser = userService.registerUser(userDto)

        userProfileService.createDefaultUserProfile(UserProfileDto().apply { userSeq = registeredUser.userSeq })

        userSettingService.createDefaultUserSettings(UserSettingDto().apply { userSeq = registeredUser.userSeq })

        val token = jwtUtil.createAccessToken(registeredUser);

        userService.addUserTokenByUserSeq(
            //임시로 refreshToken, exprieDt 지정
            UserTokenDto().apply {
                userSeq = registeredUser.userSeq
                refreshToken = token
                expiredDt = jwtUtil.getExpireDt(token.split(" ")[1])
            }
        )

        return registeredUser
    }

    override fun updateUserInfoByUser(userDto: UserDto): UserDto {
        return userService.updateUserInfoByUser(userDto)
    }


    override fun login(userDto: UserDto): LoginResponseDto {
        val userInfo = userService.getFindUserInfoByEmailAndPassword(userDto.email, userDto.passwd)

        // jwt 발급후 db애 저장
        val token = jwtUtil.createRefreshToken(userInfo)

        userService.updateJwtTokenByUserSeq(
            UserTokenDto().apply {
                userSeq = userInfo.userSeq
                refreshToken = token
                expiredDt = jwtUtil.getExpireDt(token.split(" ")[1])
            }
        )

        return LoginResponseDto().apply {
            this.userSeq = userInfo.userSeq
            this.email = userInfo.email
            this.nickName = userInfo.nickName
            this.token = token
        }

    }
}