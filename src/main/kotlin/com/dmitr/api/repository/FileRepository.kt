package com.dmitr.api.repository

import com.dmitr.api.entity.FileEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : CrudRepository<FileEntity, Long> {
}