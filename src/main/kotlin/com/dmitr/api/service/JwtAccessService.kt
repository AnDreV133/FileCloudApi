package com.dmitr.api.service

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
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
class JwtAccessService {
    @Value("\${token.access.secret}")
    private lateinit var secret: String

    @Value("\${token.access.lifetime}")
    private lateinit var tokenLifetime: Duration

    fun <T> generateToken(
        user: T,
    ): String where T : ILogin, T : ISubscriptionLevel {
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + tokenLifetime.toMillis())
        val claims = mutableMapOf<String, Any>().also {
            it[SUBSCRIPTION_LEVEL_KEY] = user.subscriptionLevel.name
        }

        return Jwts.builder()
            .claims(claims)
            .subject(user.login)
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(SecretKeySpec(secret.toByteArray(), ALGORITHM_SPEC))
            .compact()
    }

    fun getLogin(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getSubscriptionLevel(token: String): String {
        return getAllClaimsFromToken(token).get(SUBSCRIPTION_LEVEL_KEY, String::class.java)
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
        const val SUBSCRIPTION_LEVEL_KEY = "subscriptionLevel"
        const val ALGORITHM_SPEC = "HmacSHA256"
    }
}
