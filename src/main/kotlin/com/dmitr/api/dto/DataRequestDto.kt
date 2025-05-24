package com.dmitr.api.dto

data class DataRequestDto(
    val name: String,
    override val session: String
) : ISession