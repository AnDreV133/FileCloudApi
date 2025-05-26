package com.dmitr.api.entity

import jakarta.persistence.*

@Entity
class TokenPairEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,
    val tokenRefresh: String,
    val tokenAccess: String,
)