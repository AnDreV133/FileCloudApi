package com.dmitr.api.repository

import com.dmitr.api.entity.TokenPairEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
abstract class TokenRepository : CrudRepository<TokenPairEntity, Long> {
}