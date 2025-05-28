package com.dmitr.api.dto

import java.io.Serializable

data class TokenPairResponseDto(
    val tokenRefresh: String,
    val tokenAccess: String,
) : Serializable