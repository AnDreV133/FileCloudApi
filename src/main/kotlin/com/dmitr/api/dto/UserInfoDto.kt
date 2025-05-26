package com.dmitr.api.dto

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevelName

data class UserInfoDto(
    val name: String,
    override val login: String,
    override val subscriptionLevel: String
) : ILogin, ISubscriptionLevelName