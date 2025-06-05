package com.dmitr.api.dto

import java.io.Serializable

data class DataResponseDto(
    val uuid: String,
    val filename: String,
    val extension: String,
    val size: Long,
) : Serializable