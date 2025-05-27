package com.dmitr.api.dto

data class TokenPairResponseDto(
    val tokenRefresh: String,
    val tokenAccess: String,
)