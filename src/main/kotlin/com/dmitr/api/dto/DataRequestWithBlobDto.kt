package com.dmitr.api.dto

import com.dmitr.api.declaration.ISession

data class DataRequestWithBlobDto(
    val name: String,
    val blob: String,
    override val session: String
) : ISession