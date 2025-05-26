package com.dmitr.api.repository

import com.dmitr.api.entity.TokenPairEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenPairRepository : CrudRepository<TokenPairEntity, Long> {
    fun findByTokenAccess(tokenAccess: String): TokenPairEntity
}