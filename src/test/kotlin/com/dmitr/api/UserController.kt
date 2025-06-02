package com.dmitr.api

import com.dmitr.api.controller.DataController
import com.dmitr.api.dto.UserInfoDto
import com.dmitr.api.entity.SubscriptionLevelEnum
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.service.DataService
import com.dmitr.api.service.JwtService
import com.dmitr.api.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(DataController::class)
class UserController {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var jwtService: JwtService

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var dataService: DataService

    private lateinit var accessToken: String

    private val testUserInfo = UserInfoDto(
        login = TEST_LOGIN,
        name = TEST_LOGIN,
        subscriptionLevel = SubscriptionLevelEnum.STANDARD
    )

    @BeforeEach
    fun setup() {
        val mockUser = UserEntity(
            1,
            TEST_LOGIN,
            TEST_LOGIN,
            "p",
            SubscriptionLevelEnum.STANDARD
        )

        accessToken = "access_token"
        `when`(jwtService.generateAccessToken(mockUser)).thenReturn(accessToken)
        `when`(jwtService.getLoginFromAccessToken(accessToken)).thenReturn(TEST_LOGIN)
    }

    @Test
    @WithMockUser(username = TEST_LOGIN)
    fun `get user info should return 200 and user data`() {
        `when`(userService.getInfo(TEST_LOGIN)).thenReturn(testUserInfo)

        mockMvc.perform(
            get("/user/info")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.login").value(TEST_LOGIN))
            .andExpect(jsonPath("$.name").value("Test User"))
            .andExpect(jsonPath("$.subscriptionLevel").value("PREMIUM"))
    }

    @Test
    @WithMockUser(username = TEST_LOGIN)
    fun `clear user data should return 200`() {
        mockMvc.perform(delete("/user/clear"))

        verify(userService).removeAllData(TEST_LOGIN)
    }

    companion object {
        private const val TEST_LOGIN = "l"
    }
}