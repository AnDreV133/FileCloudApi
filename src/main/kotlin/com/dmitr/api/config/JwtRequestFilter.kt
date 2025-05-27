package com.dmitr.api.config

import com.dmitr.api.exception.NoTokenFoundException
import com.dmitr.api.exception.TokenAccessExpiredException
import com.dmitr.api.exception.TokenBadSignatureException
import com.dmitr.api.service.JwtAccessService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SignatureException


@Component
class JwtRequestFilter(
    private val jwtAccessService: JwtAccessService,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith("/auth")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")
        val tokenAccess =
            if (authHeader?.startsWith("Bearer ") == true)
                authHeader.substring("Bearer ".length)
            else
                throw NoTokenFoundException()

        if (jwtAccessService.validateToken(tokenAccess).not())
            throw TokenAccessExpiredException()

        val login = try {
            jwtAccessService.getLogin(tokenAccess)
        } catch (e: SignatureException) {
            throw TokenBadSignatureException()
        }

        SecurityContextHolder.getContext().let {
            if (it.authentication == null) {
                it.authentication = UsernamePasswordAuthenticationToken(login, null)
            }
        }

        filterChain.doFilter(request, response)
    }
}