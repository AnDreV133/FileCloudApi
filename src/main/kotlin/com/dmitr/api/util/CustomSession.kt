package com.dmitr.api.util

import java.util.*

class CustomSession private constructor(
    val session: String,
    val issuedAt: Date,
    val expirationTime: Date,
) {
    class builder {
        private var issuedAt: Date? = null
        private var expirationTime: Date? = null
        fun issuedAt(issuedAt: Date) = apply { this.issuedAt = issuedAt }

        fun expiredAt(expirationTime: Date) = apply { this.expirationTime = expirationTime }
        fun build(): CustomSession {
            val timeDelta = 15 * 60 * 1000

            if (issuedAt == null && expirationTime == null) {
                issuedAt = Date()
                expirationTime = Date(issuedAt!!.time + timeDelta)
            } else if (issuedAt == null) {
                issuedAt = Date(expirationTime!!.time - timeDelta)
            } else if (expirationTime == null) {
                expirationTime = Date(issuedAt!!.time + timeDelta)
            }

            return CustomSession(
                session = UUID.randomUUID().toString(),
                issuedAt = issuedAt!!,
                expirationTime = expirationTime!!
            )
        }
    }
}
