package com.dmitr.api.dto

import com.dmitr.api.declaration.IToken

data class TokenRequestDto(
    override val token: String,
) : IToken