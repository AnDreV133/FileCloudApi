package com.dmitr.api.service

import com.dmitr.api.dto.UserInfoDto
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.exception.UserNotFoundException
import com.dmitr.api.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun getInfo(login: String): UserInfoDto {
        return userRepository.findByLogin(login)?.toUserInfoDto()
            ?: throw UserNotFoundException()
    }

    @Transactional
    fun removeAllData(login: String) {
        userRepository.deleteByLogin(login)
    }

    private fun UserEntity.toUserInfoDto(): UserInfoDto {
        return UserInfoDto(
            login = login,
            name = name,
            subscriptionLevel = subscriptionLevel
        )
    }
}