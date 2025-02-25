package com.example.sosamoapp.util

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAUpdateClause

class QuerydslExtensions {
    companion object {
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
    }
}