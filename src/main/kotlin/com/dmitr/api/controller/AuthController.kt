package com.dmitr.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @PostMapping("/sign-up")
    fun signUp(): String {
        return "sign-up"
    }

    @PostMapping("/sign-in")
    fun signIn(): String {
        return "sign-in"
    }
}