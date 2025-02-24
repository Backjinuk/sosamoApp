package com.example.sosamoapp.user.user.domain.entity

import com.example.sosamoapp.user.domain.UserJoinType
import com.example.sosamoapp.user.domain.UserRole
import com.example.sosamoapp.user.domain.entity.UserEntity
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserEntityValidationTest {

 private lateinit var validator: Validator

 @BeforeEach
 fun setUp() {
  // Validator 초기화
  validator = Validation.buildDefaultValidatorFactory().validator
 }

 @Test
 fun `이메일이 없으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "" // 이메일 없음
   passwd = "validPassword123"
   nickName = "UserNick"
   userRole = UserRole.User
   joinType = UserJoinType.HOMEPAGE
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "이메일은 필수 항목입니다." })
 }

 @Test
 fun `유효한 이메일이면 벨리데이션 성공`() {
  // given
  val user = UserEntity().apply {
   email = "test123@naver.com" // 유효한 이메일
   passwd = "validPassword123"
   nickName = "UserNick"
   userRole = UserRole.User
   joinType = UserJoinType.KAKAO
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertTrue(violations.isEmpty()) // 벨리데이션 오류 없음 예상
 }

 @Test
 fun `이메일 형식이 유효하지 않으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "invalid-email-format" // 유효하지 않은 이메일 형식
   passwd = "validPassword123"
   nickName = "UserNick"
   userRole = UserRole.User
   joinType = UserJoinType.NAVER
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "이메일 형식이 유효하지 않습니다." })
 }

 @Test
 fun `비밀번호가 너무 짧으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "test@example.com"
   passwd = "short" // 너무 짧은 비밀번호
   nickName = "UserNick"
   userRole = UserRole.User
   joinType = UserJoinType.GITHUB
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "비밀번호는 8~20자 사이여야 합니다." })
 }

 @Test
 fun `비밀번호가 너무 길면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "test@example.com"
   passwd = "a".repeat(21) // 너무 긴 비밀번호
   nickName = "UserNick"
   userRole = UserRole.User
   joinType = UserJoinType.HOMEPAGE
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "비밀번호는 8~20자 사이여야 합니다." })
 }

 @Test
 fun `닉네임이 없으면 벨리데이션 실패`() {
  // given
  val user = UserEntity().apply {
   email = "test@example.com"
   passwd = "validPassword123"
   nickName = "" // 닉네임 없음
   userRole = UserRole.User
   joinType = UserJoinType.KAKAO
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertEquals(1, violations.size) // 하나의 벨리데이션 오류 예상
  assertTrue(violations.any { it.message == "닉네임은 필수 항목입니다." })
 }

 @Test
 fun `모든 필드가 유효하면 벨리데이션 성공`() {
  // given
  val user = UserEntity().apply {
   email = "valid.email@example.com"
   passwd = "ValidPass123"
   nickName = "ValidNick"
   userRole = UserRole.Admin
   joinType = UserJoinType.GITHUB
  }

  // when
  val violations: Set<ConstraintViolation<UserEntity>> = validator.validate(user)

  // then
  assertTrue(violations.isEmpty()) // 벨리데이션 오류 없음 예상
 }





}