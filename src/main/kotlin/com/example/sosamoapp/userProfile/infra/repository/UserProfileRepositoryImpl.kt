package com.example.sosamoapp.userProfile.infra.repository

import com.example.sosamoapp.userProfile.domain.entity.QSocialMediaPlatFormEntity
import com.example.sosamoapp.userProfile.domain.entity.QUserProfileEntity
import com.example.sosamoapp.userProfile.domain.entity.SocialMediaPlatFormEntity
import com.example.sosamoapp.userProfile.domain.entity.UserProfileEntity
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.jpa.impl.JPAUpdateClause
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class UserProfileRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : UserProfileRepository {

    private val qUserProfile: QUserProfileEntity = QUserProfileEntity.userProfileEntity
    private val qSocialMediaPlatForm : QSocialMediaPlatFormEntity = QSocialMediaPlatFormEntity.socialMediaPlatFormEntity


    @Transactional
    override fun userProfitableSetting(userProfile: UserProfileEntity): UserProfileEntity {
        entityManager.persist(userProfile)
        return userProfile
    }

    override fun updateUserProfileByUserProfile(userProfileEntity: UserProfileEntity): UserProfileEntity {
        val execute = queryFactory.update(qUserProfile)
            .setIfNotNull(qUserProfile.fileSeq, userProfileEntity.fileSeq)
            .setIfNotBlank(qUserProfile.introduction, userProfileEntity.introduction)
            .where(qUserProfile.userSeq.eq(userProfileEntity.userSeq))
            .execute()

        if (execute == 0L) {
            throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        }

        return userProfileEntity
    }

    override fun findUserProfileByUserSeq(userSeq : Long): UserProfileEntity {
        val userProfile = queryFactory.selectFrom(qUserProfile)
            .where(qUserProfile.userSeq.eq(userSeq))
            .fetchOne()

       if(userProfile == null) {
           throw IllegalArgumentException("존재하지 않는 사용자입니다.")
       }

        return userProfile
    }


    @Transactional
    override fun socialMediaPlatFromByUserProfile(socialMediaPlatFormEntity: SocialMediaPlatFormEntity): SocialMediaPlatFormEntity {
        entityManager.persist(socialMediaPlatFormEntity)
        return socialMediaPlatFormEntity
    }

    override fun updateSocialMediaPlatFormBySocialMediaForm(socialMediaPlatFormEntity: SocialMediaPlatFormEntity): SocialMediaPlatFormEntity {
        val execute = queryFactory.update(qSocialMediaPlatForm)
            .setIfNotBlank(qSocialMediaPlatForm.platFormName, socialMediaPlatFormEntity.platFormName)
            .setIfNotBlank(qSocialMediaPlatForm.platFormUrl, socialMediaPlatFormEntity.platFormUrl)
            .where(qSocialMediaPlatForm.socialMediaPlatFormSeq.eq(socialMediaPlatFormEntity.socialMediaPlatFormSeq))
            .execute()

        if (execute == 0L) {
            throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        }

        return socialMediaPlatFormEntity
    }

    override fun findSocialMediaPlatFormByUserProfileSeq(userProfileSeq: Long): List<SocialMediaPlatFormEntity> {
        val socialMediaPlatFromList = queryFactory.selectFrom(qSocialMediaPlatForm)
            .where(qSocialMediaPlatForm.userProfileSeq.eq(userProfileSeq))
            .fetch()

       if(socialMediaPlatFromList.isEmpty()) {
           throw IllegalArgumentException("존재하지 않는 사용자입니다.")
       }

        return  socialMediaPlatFromList
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
