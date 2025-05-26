package com.dmitr.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class AppException(message: String) : RuntimeException(message)

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User is already exists")
class UserAvailableException(message: String = "") : AppException(message)
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "User has not token")
class NoTokenFoundException(message: String = "") : AppException(message)
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Refresh token expired")
class TokenRefreshExpiredException(message: String = "") : AppException(message)
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access token expired")
class TokenAccessExpiredException(message: String = "") : AppException(message)

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "signature token is invalid")
class TokenBadSignatureException(message: String = "") : AppException(message)