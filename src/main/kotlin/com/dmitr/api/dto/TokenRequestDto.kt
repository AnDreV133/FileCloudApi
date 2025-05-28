package com.dmitr.api.dto

import com.dmitr.api.declaration.IToken
import java.io.Serializable

data class TokenRequestDto(
    override val token: String,
) : Serializable, IToken