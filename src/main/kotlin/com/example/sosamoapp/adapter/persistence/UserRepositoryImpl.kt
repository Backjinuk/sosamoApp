package com.example.sosamoapp.adapter.persistence

import com.example.sosamoapp.domain.entity.user.QUserEntity
import com.example.sosamoapp.domain.entity.user.QUserTokenEntity
import com.example.sosamoapp.domain.entity.user.UserEntity
import com.example.sosamoapp.domain.entity.user.UserTokenEntity
import com.example.sosamoapp.domain.repository.UserRepository
import com.example.sosamoapp.util.QuerydslExtensions.Companion.setIfNotBlank
import com.example.sosamoapp.util.QuerydslExtensions.Companion.setIfNotNull
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : UserRepository {

    private val qUserEntity: QUserEntity = QUserEntity.userEntity

    private val qUserTokenEntity: QUserTokenEntity = QUserTokenEntity.userTokenEntity


    override fun userJoin(user: UserEntity): UserEntity {
        entityManager.persist(user)
        return user
    }

    override fun addUserTokenByUserSeq(userTokenEntity: UserTokenEntity): UserTokenEntity {
        entityManager.persist(userTokenEntity)
        return userTokenEntity
    }

    override fun updateJwtTokenByUserSeq(userTokenEntity: UserTokenEntity?) {
        val execute = queryFactory.update(qUserTokenEntity)
            .setIfNotNull(qUserTokenEntity.refreshToken, userTokenEntity?.refreshToken)
            .setIfNotNull(qUserTokenEntity.expiredDt, userTokenEntity?.expiredDt)
            .where(qUserTokenEntity.userSeq.eq(userTokenEntity?.userSeq))
            .execute()

        if (execute == 0L) {
            throw IllegalArgumentException("존재하지 않는 사용자 입니다.")
        }

    }

    override fun getFindUserInfoByUserSeq(userSeq: Long): UserEntity {
        val userEntity = queryFactory.selectFrom(qUserEntity)
            .where(qUserEntity.userSeq.eq(userSeq))
            .fetchOne();

        if (userEntity == null){
            throw IllegalArgumentException("존재하지 않는 사용자 입니다.")
        }

        return userEntity;
    }

    override fun updateUserInfoByUser(userEntity: UserEntity): UserEntity {
        val affected = queryFactory.update(qUserEntity)
            .setIfNotNull(qUserEntity.fileSeq, userEntity.fileSeq)
            .setIfNotBlank(qUserEntity.email, userEntity.email)
            .setIfNotBlank(qUserEntity.passwd, userEntity.passwd)
            .setIfNotBlank(qUserEntity.nickName, userEntity.nickName)
            .setIfNotBlank(qUserEntity.ciKey, userEntity.ciKey)
            // Enum 필드는 기본적으로 업데이트
            .set(qUserEntity.joinType, userEntity.joinType)
            .set(qUserEntity.userRole, userEntity.userRole)
            .where(qUserEntity.userSeq.eq(userEntity.userSeq))
            .execute()

        if (affected == 0L) {
            throw IllegalArgumentException("존재하지 않는 사용자 입니다.")
        }

        return userEntity
    }


    override fun userIsExistsByEmail(email: String): Boolean {
        var result = queryFactory.select(qUserEntity.email)
            .from(qUserEntity)
            .where(qUserEntity.email.eq(email))
            .fetchFirst();

        return result != null
    }

    override fun userIsNickNameByUserDto(nickName: String): Boolean {
        var result = queryFactory.selectFrom(qUserEntity)
            .where(qUserEntity.nickName.eq(nickName))
            .fetchFirst();

        return result != null
    }

    override fun getFindUserInfoByEmailAndPasswd(email: String, passwd: String): UserEntity {
        val resultValue = queryFactory.selectFrom(qUserEntity)
            .where(qUserEntity.email.eq(email).and(qUserEntity.passwd.eq(passwd)))
            .fetchOne()

        if (resultValue == null) {
            throw IllegalArgumentException("존재하지 않는 사용자 입니다.")
        }

        return resultValue
    }
}

