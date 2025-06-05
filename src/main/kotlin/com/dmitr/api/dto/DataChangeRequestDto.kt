package com.dmitr.api.dto

import java.io.Serializable

data class DataChangeRequestDto(
    val filename: String,
) : Serializable