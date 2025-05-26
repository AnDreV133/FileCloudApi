package com.dmitr.api.entity

import jakarta.persistence.*

@Entity
class FileEntity(
    @Id
    @GeneratedValue
    val id: Long = -1,
    @ManyToOne
    val user: UserEntity,
    val name: String,
    val size: Int,
    @Lob
    val blob: ByteArray
)