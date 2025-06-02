package com.dmitr.api.dto

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import com.dmitr.api.entity.SubscriptionLevelEnum
import java.io.Serializable

data class UserInfoDto(
    val name: String,
    override val login: String,
    override val subscriptionLevel: SubscriptionLevelEnum,
) : Serializable, ILogin, ISubscriptionLevel