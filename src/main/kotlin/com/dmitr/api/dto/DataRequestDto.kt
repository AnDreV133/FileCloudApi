package com.dmitr.api.dto

import java.io.Serializable

data class DataRequestDto(
    val filename: String,
    val blob: ByteArray,
) : Serializable