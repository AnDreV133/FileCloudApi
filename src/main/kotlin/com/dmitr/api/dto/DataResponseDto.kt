package com.dmitr.api.dto

import java.io.Serializable

data class DataResponseDto(
    val uuid: String,
    val name: String,
    val extension: String,
    val size: Long,
    val blob: ByteArray? = null
) : Serializable