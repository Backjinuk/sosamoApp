package com.example.sosamoapp.user.infra.repository

import com.example.sosamoapp.user.domain.entity.UserEntity
import com.example.sosamoapp.user.domain.entity.UserTokenEntity
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {

    fun userIsExistsByEmail(email: String): Boolean

    fun userJoin(user : UserEntity): UserEntity

    fun addUserTokenByUserSeq(userTokenEntity: UserTokenEntity) : UserTokenEntity

    fun getFindUserInfoByUserSeq(userSeq: Long) : UserEntity

    fun updateUserInfoByUser(userEntity: UserEntity): UserEntity

    fun userIsNickNameByUserDto(nickName: String): Boolean

    fun getFindUserInfoByEmailAndPasswd(email: String, passwd: String) : UserEntity

    fun updateJwtTokenByUserSeq(userTokenEntity: UserTokenEntity?)


}
