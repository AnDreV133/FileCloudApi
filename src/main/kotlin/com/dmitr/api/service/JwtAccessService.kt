package com.dmitr.api.service

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Component
class JwtAccessService {
    @Value("\${token.access.secret}")
    private lateinit var secret: String

    @Value("\${token.access.lifetime}")
    private lateinit var tokenLifetime: Duration

    fun generateToken(): String {
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + tokenLifetime.toMillis())

        return Jwts.builder()
            .subject(UUID.randomUUID().toString())
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(SecretKeySpec(secret.toByteArray(), ALGORITHM_SPEC))
            .compact()
    }

    fun getExpirationDate(token: String): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSignInKey(): SecretKey {
        val bytes: ByteArray = Base64.getDecoder()
            .decode(secret.toByteArray(StandardCharsets.UTF_8))
        return SecretKeySpec(bytes, ALGORITHM_SPEC)
    }

    private companion object {
        const val ALGORITHM_SPEC = "HmacSHA256"
    }
}