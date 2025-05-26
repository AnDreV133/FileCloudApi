package com.dmitr.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @RequestMapping("sign-up")
    fun signUp(): String {
        return "sign-up"
    }

    @RequestMapping("sign-in")
    fun signIn(): String {
        return "sign-in"
    }
}