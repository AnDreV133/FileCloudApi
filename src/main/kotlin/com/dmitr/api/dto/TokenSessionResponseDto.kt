package com.dmitr.api.dto

data class TokenSessionResponseDto(
    override val token: String,
    override val session: String
) : IToken, ISession