package com.dmitr.api

import com.dmitr.api.controller.DataController
import com.dmitr.api.dto.DataChangeRequestDto
import com.dmitr.api.dto.DataRequestDto
import com.dmitr.api.dto.DataResponseDto
import com.dmitr.api.entity.SubscriptionLevelEnum
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.service.DataService
import com.dmitr.api.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@WebMvcTest(DataController::class)
class DataControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var jwtService: JwtService

    @MockBean
    private lateinit var dataService: DataService

    private lateinit var accessToken: String

    @BeforeEach
    fun setup() {
        val mockUser = UserEntity(1, TEST_LOGIN, TEST_LOGIN, "p", SubscriptionLevelEnum.STANDARD)

        accessToken = "access_token"
        `when`(jwtService.generateAccessToken(mockUser)).thenReturn(accessToken)
        `when`(jwtService.getLogin(accessToken)).thenReturn(TEST_LOGIN)
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `getDataHeaders should return 200 and data headers`() {
        val dataList = listOf(
            DataResponseDto(UUID.randomUUID().toString(), "file1", "txt", 50, null),
            DataResponseDto(UUID.randomUUID().toString(), "file2", "txt", 60, null),
        )

        `when`(dataService.getAllDataHeaders(TEST_LOGIN)).thenReturn(dataList)

        mockMvc.perform(
            get("/data")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("file1"))
            .andExpect(jsonPath("$[1].name").value("file2"))
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `getDataByUuid should return 200 and data`() {
        val uuid = UUID.randomUUID().toString()
        val content = "content".toByteArray()
        val data = DataResponseDto(uuid, "file", "txt", content.size.toLong(), content)

        `when`(dataService.getData(TEST_LOGIN, uuid)).thenReturn(data)

        mockMvc.perform(
            get("/data/$uuid")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("file"))
            .andExpect(jsonPath("$.blob").value("Y29udGVudA==")) // base64 of "content"
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `save should return 200 and saved data`() {
        val content = "content".toByteArray()
        val file = MockMultipartFile("file", "test.txt", "text/plain", content)
        val request = DataRequestDto("test.txt", content)
        val savedData = DataResponseDto(UUID.randomUUID().toString(), "test", "txt", file.bytes.size.toLong(), content)

        `when`(dataService.saveData(request, TEST_LOGIN)).thenReturn(savedData)

        mockMvc.perform(
            multipart("/data")
                .file(file)
                .param("filename", "test.txt")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer $accessToken")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("test"))
            .andExpect(jsonPath("$.size").value(file.bytes.size))
            .andExpect(jsonPath("$.blob").value("Y29udGVudA==")) // base64 of "content"
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `update should return 200 and updated data`() {
        val uuid = UUID.randomUUID().toString()
        val updateRequest = DataChangeRequestDto(
            fullName = "updated.txt",
        )
        val updatedData = DataResponseDto(
            uuid = uuid,
            name = "updated",
            extension = "txt",
            size = 1024L,
            blob = null
        )

        `when`(dataService.updateData(uuid, updateRequest, TEST_LOGIN)).thenReturn(updatedData)

        mockMvc.perform(
            put("/data/$uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
                .header("Authorization", "Bearer $accessToken")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(uuid))
            .andExpect(jsonPath("$.name").value("updated"))
            .andExpect(jsonPath("$.extension").value("txt"))
            .andExpect(jsonPath("$.blob").doesNotExist())
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `delete should return 200 and true when successful`() {
        val uuid = UUID.randomUUID().toString()

        `when`(dataService.deleteData(uuid, TEST_LOGIN)).thenReturn(true)

        mockMvc.perform(
            delete("/data/$uuid")
                .header("Authorization", "Bearer $accessToken")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    @WithMockUser(TEST_LOGIN)
    fun `delete should return 200 and false when failed`() {
        val uuid = UUID.randomUUID().toString()

        `when`(dataService.deleteData(uuid, TEST_LOGIN)).thenReturn(false)

        mockMvc.perform(
            delete("/data/$uuid")
                .header("Authorization", "Bearer $accessToken")
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }

    companion object {
        private const val TEST_LOGIN = "l"
    }
}

// todo maybe remove with(csrf()), because its crutch