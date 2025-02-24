package com.example.sosamoapp.user.infra.repository

import com.example.sosamoapp.user.domain.entity.QUserEntity
import com.example.sosamoapp.user.domain.entity.QUserTokenEntity
import com.example.sosamoapp.user.domain.entity.UserEntity
import com.example.sosamoapp.user.domain.entity.UserTokenEntity
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.jpa.impl.JPAUpdateClause
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


// String 값이 공백이 아니면 업데이트, 아니면 체인을 그대로 반환
fun JPAUpdateClause.setIfNotBlank(field: StringPath, value: String): JPAUpdateClause {
    return if (value.isNotBlank()) {
        this.set(field, value)
    } else {
        this
    }
}

// 널이 아닌 값을 업데이트 (필드의 타입은 제네릭으로 처리)
fun <T> JPAUpdateClause.setIfNotNull(field: Path<T>, value: T?): JPAUpdateClause {
    return if (value != null) {
        this.set(field, value)
    } else {
        this
    }
}