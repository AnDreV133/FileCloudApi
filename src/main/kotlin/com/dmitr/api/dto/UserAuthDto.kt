package com.dmitr.api.dto

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.IPassword

data class UserAuthDto(
    override val login: String,
    override val password: String
): ILogin, IPassword