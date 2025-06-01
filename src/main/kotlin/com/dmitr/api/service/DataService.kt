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
        val fileNameParts = data.fullName.toFilenameParts()
        val hasEqualFilename = dataRepository.findByUserAndNameAndExtension(
            user,
            fileNameParts.first,
            fileNameParts.second,
        )

        if (hasEqualFilename) {
            throw FilenameEqualException()
        }

        val dataEntity = DataEntity(
            name = fileNameParts.first,
            extension = fileNameParts.second,
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
        val fileNameParts = data.fullName.toFilenameParts()
        val hasEqualFilename = dataRepository.findByUserAndNameAndExtension(
            user,
            fileNameParts.first,
            fileNameParts.second,
        )

        if (hasEqualFilename) {
            throw FilenameEqualException()
        }

        val oldDataEntity = dataRepository.findByUserAndUuid(user, uuid) ?: throw FileNotFoundException()

        val newDataEntity = DataEntity(
            name = fileNameParts.first,
            extension = fileNameParts.second,
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
        name = name,
        extension = extension,
        size = length
    )

    private fun String.toFilenameParts(): Pair<String, String> {
        return substringBeforeLast(".") to substringAfterLast(".")
    }
}
