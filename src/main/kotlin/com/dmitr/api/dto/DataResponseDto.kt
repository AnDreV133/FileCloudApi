package com.dmitr.api.dto

data class DataResponseDto(
    val name: String,
    val extension: String,
    val size: Int,
    val blob: String? = null
)