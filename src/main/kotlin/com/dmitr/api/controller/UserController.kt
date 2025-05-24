package com.dmitr.api.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun get() {

    }

    @PutMapping
    fun update() {

    }

    @DeleteMapping
    fun logout() {

    }

    @DeleteMapping("/clear")
    fun clearUserAndData() {

    }
}