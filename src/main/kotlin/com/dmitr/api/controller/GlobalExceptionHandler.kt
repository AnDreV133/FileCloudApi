package com.dmitr.api.controller

import com.dmitr.api.exception.AppException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ex.message, ex.statusCode)
    }
}
