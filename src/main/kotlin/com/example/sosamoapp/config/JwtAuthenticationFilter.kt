//package com.example.sosamoapp.config
//
//import com.example.sosamoapp.util.JwtUtil
//import io.jsonwebtoken.MalformedJwtException
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.slf4j.LoggerFactory
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
//import java.security.SignatureException
//
//class JwtAuthenticationFilter(
//    private val jwtUtil: JwtUtil,
//    authenticationManager: AuthenticationManager
//) : BasicAuthenticationFilter(authenticationManager) {
//
//    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
//
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        chain: FilterChain
//    ) {
//        val header = request.getHeader("AccessToken")
//        log.debug("Authorization Header: $header")
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            log.debug("No JWT token found in request headers")
//            chain.doFilter(request, response)
//            return
//        }
//
//        try {
//            val token = header.replace("Bearer ", "").replace(" ", "")
//            log.debug("JWT Token: $token")
//            val claims = jwtUtil.parseClaims(mapOf("AccessToken" to token))
//            log.debug("JWT Claims: $claims")
//
//            if (claims != null) {
//                val userId = claims["userSeq"].toString().toLong()
//                val authorities: MutableList<GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
//                val auth = UsernamePasswordAuthenticationToken(userId, null, authorities)
//                SecurityContextHolder.getContext().authentication = auth
//                log.debug("Authenticated user: $userId")
//            }
//        } catch (e: SignatureException) {
//            log.error("Invalid JWT signature: ${e.message}")
//        } catch (e: MalformedJwtException) {
//            log.error("Invalid JWT token: ${e.message}")
//        } catch (e: Exception) {
//            log.error("Error parsing JWT token: ${e.message}")
//        }
//
//        chain.doFilter(request, response)
//    }
//}