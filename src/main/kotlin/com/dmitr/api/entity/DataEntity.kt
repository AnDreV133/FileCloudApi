package com.dmitr.api.entity

import com.dmitr.api.projection.DataHeaderProjection
import jakarta.persistence.*
import java.util.*

@Entity
class DataEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val uuid: String = "",
    @ManyToOne
    override val user: UserEntity,
    override val filename: String,
    override val extension: String,
    override val length: Long,
    @Lob
    val blobData: ByteArray
): DataHeaderProjection