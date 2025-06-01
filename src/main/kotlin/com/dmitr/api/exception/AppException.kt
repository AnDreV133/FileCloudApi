package com.dmitr.api.exception

import org.springframework.http.HttpStatus

sealed class AppException(
    val statusCode: HttpStatus,
    override val message: String,
) : RuntimeException("${statusCode.value()}: $message")

class UserAvailableException(login: String = "") : AppException(HttpStatus.CONFLICT, "User ($login) is already exists")
class UserNotFoundException(login: String = "") : AppException(HttpStatus.NOT_FOUND, "User ($login) is not found")
class NoTokenFoundException : AppException(HttpStatus.UNAUTHORIZED, "User has not token")
class TokenRefreshExpiredException : AppException(HttpStatus.FORBIDDEN, "Refresh token expired")
class TokenAccessExpiredException : AppException(HttpStatus.FORBIDDEN, "Access token expired")
class TokenBadSignatureException : AppException(HttpStatus.BAD_REQUEST, "signature token is invalid")
class FilenameEqualException: AppException(HttpStatus.CONFLICT, "Filename is equal")
class FileUnsavedException: AppException(HttpStatus.BAD_REQUEST, "File is not saved")