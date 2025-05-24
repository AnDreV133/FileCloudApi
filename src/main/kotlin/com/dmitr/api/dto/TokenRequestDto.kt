package com.dmitr.api.dto

data class TokenRequestDto(
    override val token: String,
) : IToken