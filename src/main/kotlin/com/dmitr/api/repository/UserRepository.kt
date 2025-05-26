package com.dmitr.api.repository

import com.dmitr.api.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {
    abstract fun findByName(name: String): UserEntity?
    abstract fun findByLogin(login: String): UserEntity?

}