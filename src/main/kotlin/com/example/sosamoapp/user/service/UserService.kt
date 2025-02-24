package com.example.sosamoapp.user.service

import com.example.sosamoapp.user.infra.repository.UserRepository
import com.example.sosamoapp.user.domain.dto.UserDto
import com.example.sosamoapp.user.domain.dto.UserTokenDto
import com.example.sosamoapp.user.domain.entity.UserEntity
import com.example.sosamoapp.user.domain.entity.UserTokenEntity
import com.example.sosamoapp.util.ValidatorUtil
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val modelMapper: ModelMapper,
    private val validatorUtil: ValidatorUtil, // ValidatorUtil 추가
) {

    fun registerUser(userDto: UserDto): UserDto {
        validatorUtil.validator(userDto) // ValidatorUtil 사용

        val userEntity = userRepository.userJoin(modelMapper.map(userDto, UserEntity::class.java))
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    fun userIsExistsByEmail(userDto: UserDto): Boolean {
        return userRepository.userIsExistsByEmail(userDto.email)
    }

    fun addUserTokenByUserSeq(userTokenDto: UserTokenDto): UserTokenDto {
        validatorUtil.validator(userTokenDto) // ValidatorUtil 사용

        val userTokenEntity = userRepository.addUserTokenByUserSeq(modelMapper.map(userTokenDto, UserTokenEntity::class.java))
        return modelMapper.map(userTokenEntity, UserTokenDto::class.java)
    }

    fun updateUserInfoByUser(userDto: UserDto): UserDto {
        validatorUtil.validator(userDto)

        val userEntity = userRepository.updateUserInfoByUser(modelMapper.map(userDto, UserEntity::class.java))
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    fun getFindUserInfoByUserSeq(userSeq: Long): UserDto {
       if(userSeq <= 0){
           throw IllegalArgumentException("유저 시퀸스는 양수여야 합니다.")
       }

        val userEntity = userRepository.getFindUserInfoByUserSeq(userSeq)
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    fun userIsNickNameByUserDto(userDto: UserDto) : Boolean {
       return userRepository.userIsNickNameByUserDto(userDto.nickName);
    }

    fun getFindUserInfoByEmailAndPassword(email: String, passwd: String) : UserDto {
        val userEntity = userRepository.getFindUserInfoByEmailAndPasswd(email, passwd)
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    fun updateJwtTokenByUserSeq(userTokenDto: UserTokenDto) {
       userRepository.updateJwtTokenByUserSeq(modelMapper.map(userTokenDto, UserTokenEntity::class.java));
    }


}