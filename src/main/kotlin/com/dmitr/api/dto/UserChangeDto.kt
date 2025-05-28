package com.dmitr.api.dto

import java.io.Serializable

data class UserChangeDto(
    val name: String? = null,
    val login: String? = null,
    val password: String? = null,
    val subscriptionLevel: String? = null,
) : Serializable