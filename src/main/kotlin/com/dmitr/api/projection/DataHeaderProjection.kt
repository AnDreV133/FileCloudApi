package com.dmitr.api.projection

import com.dmitr.api.entity.UserEntity

interface DataHeaderProjection {
    val uuid: String
    val user: UserEntity
    val filename: String
    val extension: String
    val length: Long
}