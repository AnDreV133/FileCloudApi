package com.dmitr.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @RequestMapping("sign-up")
    fun signUp() {

    }

    @RequestMapping("sign-in")
    fun signIn() {

    }
}