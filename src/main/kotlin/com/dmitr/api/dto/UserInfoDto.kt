package com.dmitr.api.dto

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import com.dmitr.api.entity.SubscriptionLevelEnum

data class UserInfoDto(
    val name: String,
    override val login: String,
    val subscriptionLevelName: String
) : ILogin, ISubscriptionLevel {
    override val subscriptionLevel = SubscriptionLevelEnum.valueOf(subscriptionLevelName)
}