package com.dmitr.api.config

import com.dmitr.api.exception.AppException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class ExceptionHandlingFilter(
    @Qualifier("handlerExceptionResolver")
    private val exceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response);
        } catch (e: AppException) {
            exceptionResolver.resolveException(request, response, null, e)
        }
    }
}