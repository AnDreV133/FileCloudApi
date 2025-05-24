package com.dmitr.api.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class UserEntity(
    @Id
    @GeneratedValue
    val id: Long,
    val login: String,
    val name: String,
    val password: String
)