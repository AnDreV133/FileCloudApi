package com.dmitr.api.dto

data class DataRequestWithBlobDto(
    val name: String,
    val blob: String,
    override val session: String
) : ISession