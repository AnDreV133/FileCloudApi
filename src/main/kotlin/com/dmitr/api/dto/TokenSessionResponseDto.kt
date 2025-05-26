package com.dmitr.api.dto

data class TokenSessionResponseDto(
    val tokenRefresh: String,
    val tokenAccess: String,
)