package com.example.sosamoapp.userSetting.infra.repository

import com.example.sosamoapp.userSetting.domain.entity.QUserSettingEntity
import com.example.sosamoapp.userSetting.domain.entity.UserSettingEntity
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.jpa.impl.JPAUpdateClause
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository


@Repository
class UserSettingRepositoryImpl (
    private val entityManager: EntityManager,
    private val queryFactory : JPAQueryFactory
) : UserSettingRepository {

    private val qUserSettingEntity : QUserSettingEntity = QUserSettingEntity.userSettingEntity

    override fun createDefaultUserSetting(savedEntity: UserSettingEntity): UserSettingEntity {
        entityManager.persist(savedEntity)
        return savedEntity
    }

    override fun updateUserSettingByUserSetting(userSettingEntity: UserSettingEntity): UserSettingEntity {
        val execute = queryFactory.update(qUserSettingEntity)
            .setIfNotNull(qUserSettingEntity.notificationEnabled, userSettingEntity.notificationEnabled)
            .setIfNotNull(qUserSettingEntity.eventEnabled, userSettingEntity.eventEnabled)
            .setIfNotNull(qUserSettingEntity.themePreference, userSettingEntity.themePreference)
            .where(qUserSettingEntity.userSeq.eq(userSettingEntity.userSeq))
            .execute();


       if(execute == 0L) {
           throw IllegalArgumentException("존재하지 않는 사용자입니다.")
       }

        return userSettingEntity
    }

    override fun findUserSettingByUserSeq(updateUserSeq: Long): UserSettingEntity {
        val resultValue = queryFactory.selectFrom(qUserSettingEntity)
            .where(qUserSettingEntity.userSeq.eq(updateUserSeq))
            .fetchOne()

        if (resultValue == null) {
            throw IllegalArgumentException("존재하지 않는 사용자입니다.")
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
