package com.dmitr.api.repository

import com.dmitr.api.entity.DataEntity
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.projection.DataHeaderProjection
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DataRepository : CrudRepository<DataEntity, Long> {
    fun getByUser(user: UserEntity): List<DataHeaderProjection>
    fun getByUserAndUuid(user: UserEntity, uuid: String): DataEntity?
    fun existsByUserAndFilename(user: UserEntity, filename: String): Boolean
    fun findByUserAndUuid(user: UserEntity, uuid: String): DataEntity?
    fun deleteByUserAndUuid(user: UserEntity, uuid: String): Int
}