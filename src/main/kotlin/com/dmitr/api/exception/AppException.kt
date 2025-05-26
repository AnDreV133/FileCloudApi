package com.dmitr.api.exception

sealed class AppException(message: String) : Exception(message)

class UserAvailableException(message: String) : AppException(message)