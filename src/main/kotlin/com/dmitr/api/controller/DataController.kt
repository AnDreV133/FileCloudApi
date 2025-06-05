package com.dmitr.api.controller

import com.dmitr.api.dto.DataChangeRequestDto
import com.dmitr.api.dto.DataRequestDto
import com.dmitr.api.dto.DataResponseDto
import com.dmitr.api.service.DataService
import com.dmitr.api.util.getUserLoginFromSecurityContext
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/data")
class DataController(
    private val dataService: DataService,
) {
    val login get() = getUserLoginFromSecurityContext()

    private val logger = LoggerFactory.getLogger(DataController::class.java)

    @GetMapping
    fun getDataList(): ResponseEntity<List<DataResponseDto>> {
        val headers = dataService.getAllDataHeaders(login)
        logger.info("Getting data headers for user: $login")
        return ResponseEntity(headers, HttpStatus.OK)
    }

    @GetMapping("/{uuid}")
    fun getFileByUuid(@PathVariable uuid: String): ResponseEntity<Resource> {
        val data = dataService.getData(login, uuid) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val resource: Resource = ByteArrayResource(data.blobData)
        val headers = HttpHeaders().apply {
            contentLength = data.length
            contentType = MediaType.APPLICATION_OCTET_STREAM
            contentDisposition = ContentDisposition.attachment()
                .filename(data.filename, StandardCharsets.UTF_8)
                .build()
        }

        logger.info("Getting data by uuid: $uuid")
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(resource)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun save(
        @RequestParam file: MultipartFile,
        @RequestParam filename: String,
    ): ResponseEntity<DataResponseDto> {
        val request = DataRequestDto(
            filename = filename,
            blob = file.bytes
        )
        val resultOfSave = dataService.saveData(request, login)
        logger.info("Saving data for user: $login")
        return ResponseEntity(resultOfSave, HttpStatus.OK)
    } // blob = null

    @PutMapping("/{uuid}")
    fun update(@PathVariable uuid: String, @RequestBody data: DataChangeRequestDto): ResponseEntity<DataResponseDto> {
        val resultOfSave = dataService.updateData(uuid, data, login)
        logger.info("Updating data for user: $login")
        return ResponseEntity(resultOfSave, HttpStatus.OK)
    } // blob = null

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: String): ResponseEntity<Boolean> {
        val resultOfDelete = dataService.deleteData(uuid, login)
        logger.info("Deleting data for user: $login")
        return ResponseEntity(resultOfDelete, HttpStatus.OK)
    }
}