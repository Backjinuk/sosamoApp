package com.example.sosamoapp.application.usecase

import com.example.sosamoapp.adapter.web.dto.LoginResponseDto
import com.example.sosamoapp.domain.dto.user.UserDto
import org.springframework.stereotype.Service

@Service
interface UserUseCase {

    // 회원가입 기능
    fun registerUser(userDto: UserDto): UserDto

    fun updateUserInfoByUser(userDto: UserDto): UserDto

    // 로그인 기능
    fun login(userDto: UserDto): LoginResponseDto
}