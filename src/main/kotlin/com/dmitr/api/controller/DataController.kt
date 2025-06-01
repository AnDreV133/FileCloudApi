package com.dmitr.api.controller

import com.dmitr.api.dto.DataRequestDto
import com.dmitr.api.dto.DataResponseDto
import com.dmitr.api.service.DataService
import com.dmitr.api.util.getUserLoginFromSecurityContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/data")
class DataController(
    private val dataService: DataService,
) {
    val login get() = getUserLoginFromSecurityContext()

    @GetMapping
    fun getDataHeaders(): ResponseEntity<List<DataResponseDto>> {
        val headers = dataService.getAllDataHeaders(login)

        return ResponseEntity(headers, HttpStatus.OK)
    }

    @GetMapping("/{uuid}")
    fun getDataByUuid(@PathVariable uuid: String): ResponseEntity<DataResponseDto> {
        val data = dataService.getData(login, uuid)

        return ResponseEntity(data, HttpStatus.OK)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun save(
        @RequestParam file: MultipartFile,
        @RequestParam filename: String
    ): ResponseEntity<DataResponseDto> {
        val request = DataRequestDto(
            fullName = filename,
            blob = file.bytes
        )
        val resultOfSave = dataService.saveData(request, login)

        return ResponseEntity(resultOfSave, HttpStatus.OK)
    }

    @PutMapping("/{uuid}")
    fun update(data: Nothing) {

    }

    @DeleteMapping("/{id}")
    fun delete() {

    }
}