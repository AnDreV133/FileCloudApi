package com.dmitr.api.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
data class TokenSessionEntity(
    @Id
    @ManyToOne
    val user: UserEntity,
    val token: String,
    val expireToken: LocalDate,
    val session: String,
    val expireSession: LocalDate
)