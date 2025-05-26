package com.dmitr.api.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class TokenPairEntity(
    @Id
    @OneToOne
    val user: UserEntity,
    val tokenRefresh: String,
    val tokenAccess: String,
)