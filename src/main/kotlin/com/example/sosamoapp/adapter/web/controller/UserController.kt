package com.example.sosamoapp.adapter.web.controller

import com.example.sosamoapp.adapter.web.dto.LoginResponseDto
import com.example.sosamoapp.application.usecase.UserUseCase
import com.example.sosamoapp.domain.dto.user.UserDto
import com.example.sosamoapp.util.JwtUtil
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userUseCase: UserUseCase,
    private val jwtUtil: JwtUtil
) {


    @RequestMapping("/join")
    fun userJoin(@RequestBody userDto: UserDto) : LoginResponseDto{
        val registerUser = userUseCase.registerUser(userDto);

        return LoginResponseDto().apply {
            userSeq = registerUser.userSeq
            email = registerUser.email
            nickName = registerUser.nickName
            token = jwtUtil.createAccessToken(registerUser);
        }
    }

    @RequestMapping("/login")
    fun  userLogin(@RequestBody userDto: UserDto) : LoginResponseDto {
        val login = userUseCase.login(userDto);
        return login;
    }


}