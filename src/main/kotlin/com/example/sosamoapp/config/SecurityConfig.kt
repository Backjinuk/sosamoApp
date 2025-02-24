//package com.example.sosamoapp.config
//
//import com.example.sosamoapp.util.JwtUtil
//import org.slf4j.LoggerFactory
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
//import kotlin.jvm.java
//
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig(private val jwtUtil: JwtUtil) {
//
//    private val log = LoggerFactory.getLogger(JwtUtil::class.java)
//
//    @Bean
//    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
//        return authenticationConfiguration.authenticationManager
//    }
//
//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
//        http.csrf { it.disable() }
//            .httpBasic { it.disable() }
//            .formLogin { it.disable() }
//            .authorizeHttpRequests { authorize ->
//                authorize.requestMatchers("/user/userJoin", "/", "/user/userLogin", "/user/JwtTokenGetUserSeq", "/chat/test").permitAll()
//                    .anyRequest().authenticated()
//            }
//            .logout { logout ->
//                logout.logoutSuccessUrl("/userLogin")
//                    .invalidateHttpSession(true)
//            }
//            .sessionManagement { session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }
//            .addFilterBefore(JwtAuthenticationFilter(jwtUtil, authenticationManager), BasicAuthenticationFilter::class.java)
//
//        log.debug("Security configuration loaded with JWT filter")
//        return http.build()
//    }
//
//
//    @Bean
//    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
//        return BCryptPasswordEncoder();
//    }
//
//
//
//}