package com.dmitr.api.repository

import com.dmitr.api.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {
    fun findByName(name: String): UserEntity?
    fun findByLogin(login: String): UserEntity?
    fun findByLoginAndPassword(login: String, password: String): UserEntity?
}