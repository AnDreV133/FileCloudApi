package com.dmitr.api.entity

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.IPassword
import com.dmitr.api.declaration.ISubscriptionLevel
import jakarta.persistence.*

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,
    override val login: String,
    val name: String,
    override val password: String,
    @Enumerated(EnumType.STRING)
    override val subscriptionLevel: SubscriptionLevelEnum
) : ILogin, IPassword, ISubscriptionLevel