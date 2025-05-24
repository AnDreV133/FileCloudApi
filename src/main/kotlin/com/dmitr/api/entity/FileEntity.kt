package com.dmitr.api.entity

import jakarta.persistence.*

@Entity
data class FileEntity(
    @Id
    @GeneratedValue
    val id: Long,
    @ManyToOne
    val user: UserEntity,
    val name: String,
    val size: Int,
    @Lob
    val blob: ByteArray
)