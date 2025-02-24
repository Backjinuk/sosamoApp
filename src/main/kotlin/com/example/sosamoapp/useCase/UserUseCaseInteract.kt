package com.example.sosamoapp.useCase


import com.example.sosamoapp.userSetting.service.UserSettingService
import com.example.sosamoapp.user.controller.dto.LoginResponseDto
import com.example.sosamoapp.user.domain.dto.UserDto
import com.example.sosamoapp.user.domain.dto.UserTokenDto
import com.example.sosamoapp.user.service.UserService
import com.example.sosamoapp.userProfile.domain.dto.UserProfileDto
import com.example.sosamoapp.userProfile.service.UserProfileService
import com.example.sosamoapp.userSetting.domain.dto.UserSettingDto
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