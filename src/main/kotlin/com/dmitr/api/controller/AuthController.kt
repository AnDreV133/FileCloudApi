package com.dmitr.api.controller

import com.dmitr.api.dto.TokenPairResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.service.LoginService
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
    @PostMapping("/sign-up")
    fun signUp(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = loginService.createUser(user)
        return ResponseEntity(tokenPair, HttpStatus.CREATED)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = loginService.updateUser(user)
        return ResponseEntity(tokenPair, HttpStatus.ACCEPTED)
    }

    @PostMapping("/new-token")
    fun refreshToken(@RequestBody refreshToken: String): ResponseEntity<String> {
        val newAccessToken = loginService.getNewAccessToken(refreshToken)
        return ResponseEntity(newAccessToken, HttpStatus.ACCEPTED)
    }
}