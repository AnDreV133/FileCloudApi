package com.dmitr.api.dto

import com.dmitr.api.declaration.ISession

data class DataRequestDto(
    val name: String,
    override val session: String
) : ISession