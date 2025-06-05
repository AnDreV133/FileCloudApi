package com.dmitr.api.service

import com.dmitr.api.dto.DataChangeRequestDto
import com.dmitr.api.dto.DataRequestDto
import com.dmitr.api.dto.DataResponseDto
import com.dmitr.api.entity.DataEntity
import com.dmitr.api.exception.FileNotFoundException
import com.dmitr.api.exception.FileUnsavedException
import com.dmitr.api.exception.FilenameEqualException
import com.dmitr.api.exception.UserNotFoundException
import com.dmitr.api.projection.DataHeaderProjection
import com.dmitr.api.repository.DataRepository
import com.dmitr.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class DataService(
    private val dataRepository: DataRepository,
    private val userRepository: UserRepository,
) {
    fun getAllDataHeaders(login: String): List<DataResponseDto> = userRepository.findByLogin(login)?.let { user ->
        dataRepository
            .getByUser(user)
            .map { dataHeader -> dataHeader.toResponseDto() }
    } ?: emptyList()

    fun getData(login: String, uuid: String): DataResponseDto? = userRepository.findByLogin(login)?.let { user ->
        dataRepository.getByUserAndUuid(user, uuid)?.toResponseDto()
    }

    fun saveData(data: DataRequestDto, login: String): DataResponseDto {
        val user = userRepository.findByLogin(login) ?: throw UserNotFoundException()
        val fileExtension = data.filename.getFileExtension()
        val hasEqualFilename = dataRepository.existsByUserAndFilenameAndExtension(
            user,
            data.filename,
            fileExtension
        )

        if (hasEqualFilename) {
            throw FilenameEqualException()
        }

        val dataEntity = DataEntity(
            filename = data.filename,
            extension = fileExtension,
            length = data.blob.size.toLong(),
            blobData = data.blob,
            user = user
        )

        return try {
            dataRepository.save(dataEntity)
        } catch (e: Exception) {
            throw FileUnsavedException()
        }.toResponseDto()
    }

    fun updateData(uuid: String, data: DataChangeRequestDto, login: String): DataResponseDto {
        val user = userRepository.findByLogin(login) ?: throw UserNotFoundException()
        val fileExtension = data.filename.getFileExtension()
        val hasEqualFilename = dataRepository.existsByUserAndFilenameAndExtension(
            user,
            data.filename,
            fileExtension,
        )

        if (hasEqualFilename) {
            throw FilenameEqualException()
        }

        val oldDataEntity = dataRepository.findByUserAndUuid(user, uuid) ?: throw FileNotFoundException()

        val newDataEntity = DataEntity(
            filename = data.filename,
            extension = fileExtension,
            length = oldDataEntity.length,
            blobData = oldDataEntity.blobData,
            user = oldDataEntity.user
        )

        return try {
            dataRepository.save(newDataEntity)
        } catch (e: Exception) {
            throw FileUnsavedException()
        }.toResponseDto()
    }

    fun deleteData(uuid: String, login: String): Boolean {
        val user = userRepository.findByLogin(login) ?: throw UserNotFoundException()

        val amountRemovedData = dataRepository.deleteByUserAndUuid(user, uuid)

        return amountRemovedData != 0
    }

    private fun DataHeaderProjection.toResponseDto() = DataResponseDto(
        uuid = uuid,
        filename = filename,
        extension = extension,
        size = length
    )

    private fun String.getFileExtension(): String {
        return substringAfterLast(".")
    }
}
