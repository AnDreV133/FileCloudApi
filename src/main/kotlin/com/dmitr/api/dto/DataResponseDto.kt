package com.dmitr.api.dto

import java.io.Serializable

data class DataResponseDto(
    val name: String,
    val extension: String,
    val size: Int,
    val blob: ByteArray? = null
) : Serializable