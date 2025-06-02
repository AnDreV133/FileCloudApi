package com.dmitr.api.service

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Service
class JwtService {
    @Value("\${token.access.secret}")
    private lateinit var accessSecret: SecretString

    @Value("\${token.access.lifetime}")
    private lateinit var accessLifetime: Duration

    @Value("\${token.refresh.secret}")
    private lateinit var refreshSecret: SecretString

    @Value("\${token.refresh.lifetime}")
    private lateinit var refreshLifetime: Duration

    fun getSecretKey(secret: String): SecretKey = SecretKeySpec(
        Base64.getDecoder()
            .decode(secret.toByteArray(StandardCharsets.UTF_8)),
        ALGORITHM_SPEC
    )

    fun <T> generateAccessToken(
        user: T,
    ): String where T : ILogin, T : ISubscriptionLevel {
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + accessLifetime.toMillis())
        val claims = mutableMapOf<String, Any>().also {
            it[SUBSCRIPTION_LEVEL_KEY] = user.subscriptionLevel.name
        }

        return Jwts.builder()
            .claims(claims)
            .subject(user.login)
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(getSecretKey(accessSecret))
            .compact()
    }

    fun <T> generateRefreshToken(user: T): String where T : ILogin {
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + refreshLifetime.toMillis())
        val claims = mutableMapOf<String, Any>().also {
            it[LOGIN_KEY] = user.login
        }

        return Jwts.builder()
            .claims(claims)
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(getSecretKey(refreshSecret))
            .compact()
    }

    fun getLoginFromAccessToken(token: String): String {
        return getAllClaimsFromToken(token, accessSecret).subject
    }

    fun getLoginFromRefreshToken(token: String): String {
        return getAllClaimsFromToken(token, accessSecret).get(LOGIN_KEY, String::class.java)
    }

    fun getSubscriptionLevel(token: String): String {
        return getAllClaimsFromToken(token, accessSecret).get(SUBSCRIPTION_LEVEL_KEY, String::class.java)
    }

//    fun getAccessExpirationDate(token: String): Date {
//        return getAllClaimsFromToken(token, accessSecret).expiration
//    }
//
//    fun getRefreshExpirationDate(token: String): Date {
//        return getAllClaimsFromToken(token, refreshSecret).expiration
//    }

    private fun getAllClaimsFromToken(token: String, secret: SecretString): Claims {
        return Jwts.parser()
            .verifyWith(getSecretKey(secret))
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private companion object {
        const val SUBSCRIPTION_LEVEL_KEY = "subscriptionLevel"
        const val LOGIN_KEY = "login"
        const val ALGORITHM_SPEC = "HmacSHA256"
    }

}

typealias SecretString = String
