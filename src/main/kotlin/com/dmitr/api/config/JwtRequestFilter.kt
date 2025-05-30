package com.dmitr.api.config

import com.dmitr.api.exception.NoTokenFoundException
import com.dmitr.api.exception.TokenAccessExpiredException
import com.dmitr.api.exception.TokenBadSignatureException
import com.dmitr.api.service.JwtService
import io.jsonwebtoken.JwtException
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
    private val jwtService: JwtService,
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

        val login = try {
            jwtService.getLogin(tokenAccess)
        } catch (e: JwtException) {
            throw TokenAccessExpiredException()
        } catch (e: SignatureException) {
            throw TokenBadSignatureException()
        }

        SecurityContextHolder.getContext().let {
            if (it.authentication == null) {
                it.authentication = UsernamePasswordAuthenticationToken(login, null, emptyList())
            }
        }

        filterChain.doFilter(request, response)
    }
}