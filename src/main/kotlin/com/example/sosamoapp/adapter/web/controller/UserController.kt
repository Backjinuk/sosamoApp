package com.example.sosamoapp.adapter.web.controller

import com.example.sosamoapp.application.usecase.UserUseCase
import com.example.sosamoapp.domain.dto.user.UserDto
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userUseCase: UserUseCase
) {


    @RequestMapping("/join")
    fun userJoin(@RequestBody userDto: UserDto){
        userUseCase.registerUser(userDto);
    }
}