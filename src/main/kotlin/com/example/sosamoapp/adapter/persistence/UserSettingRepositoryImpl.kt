package com.example.sosamoapp.adapter.persistence

import com.example.sosamoapp.domain.entity.userSetting.QUserSettingEntity
import com.example.sosamoapp.domain.entity.userSetting.UserSettingEntity
import com.example.sosamoapp.domain.repository.UserSettingRepository
import com.example.sosamoapp.util.QuerydslExtensions.Companion.setIfNotNull
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
