package com.dmitr.api.dto

import com.dmitr.api.declaration.ISession
import java.io.Serializable

data class DataRequestDto(
    val name: String,
    override val session: String
) : Serializable, ISession