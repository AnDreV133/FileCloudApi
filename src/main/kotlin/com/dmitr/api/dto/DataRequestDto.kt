package com.dmitr.api.dto

import java.io.Serializable

data class DataRequestDto(
    val fullName: String,
    val blob: ByteArray,
) : Serializable