package com.dmitr.api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Service
class JwtRefreshService {
    @Value("\${token.refresh.secret}")
    private lateinit var secret: String

    @Value("\${token.refresh.lifetime}")
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

    fun validateToken(token: String): Boolean {
        return try {
            getExpirationDate(token).before(Date())
        } catch (e: JwtException) {
            false
        }
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