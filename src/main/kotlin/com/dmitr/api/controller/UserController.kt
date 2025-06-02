package com.dmitr.api.controller

import com.dmitr.api.dto.UserInfoDto
import com.dmitr.api.service.UserService
import com.dmitr.api.util.getUserLoginFromSecurityContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    val login get() = getUserLoginFromSecurityContext()

    @GetMapping("/info")
    fun get(): ResponseEntity<UserInfoDto> {
        return ResponseEntity(userService.getInfo(login), HttpStatus.OK)
    }

//    @PutMapping
//    fun update() {
//
//    }

    @DeleteMapping("/clear")
    fun clearUserAndData(): ResponseEntity<Unit> {
        userService.removeAllData(login)

        return ResponseEntity(HttpStatus.OK)
    }
}