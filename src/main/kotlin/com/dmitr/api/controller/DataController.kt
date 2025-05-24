package com.dmitr.api.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/data")
class DataController {
    @GetMapping
    fun getList() {

    }

    @GetMapping("/{id}")
    fun getById() {

    }

    @PostMapping()
    fun save() {}

    @PutMapping("/{id}")
    fun update() {

    }

    @DeleteMapping("/{id}")
    fun delete() {

    }
}