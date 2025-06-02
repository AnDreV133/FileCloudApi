package com.dmitr.api.controller

import com.dmitr.api.dto.TokenPairResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = userService.createUser(user)
        return ResponseEntity(tokenPair, HttpStatus.CREATED)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody user: UserAuthDto): ResponseEntity<TokenPairResponseDto> {
        val tokenPair = userService.updateUser(user)
        return ResponseEntity(tokenPair, HttpStatus.ACCEPTED)
    }

    @PostMapping("/new-token")
    fun refreshToken(@RequestBody refreshToken: String): ResponseEntity<String> {
        val newAccessToken = userService.getNewAccessToken(refreshToken)
        return ResponseEntity(newAccessToken, HttpStatus.ACCEPTED)
    }
}