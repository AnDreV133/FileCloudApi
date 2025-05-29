package com.dmitr.api.controller

import com.dmitr.api.dto.DataResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/data")
class DataController {
    @GetMapping
    fun getList(): ResponseEntity<List<DataResponseDto>> {
        return TODO()
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