package com.dmitr.api.controller

import com.dmitr.api.dto.TokenPairResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.service.LoginService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService,
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/sign-up")
    fun signUp(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = loginService.createUser(user)
        logger.info("User with id ${user.login} signed up")
        return ResponseEntity(tokenPair, HttpStatus.CREATED)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = loginService.updateUser(user)
        logger.info("User with id ${user.login} signed in")
        return ResponseEntity(tokenPair, HttpStatus.ACCEPTED)
    }

    @PostMapping("/new-token")
    fun refreshToken(@RequestBody refreshToken: String): ResponseEntity<String> {
        val newAccessToken = loginService.getNewAccessToken(refreshToken)
        logger.info("User refreshed token")
        return ResponseEntity(newAccessToken, HttpStatus.ACCEPTED)
    }
}