package com.dmitr.api.service

import com.dmitr.api.dto.DataRequestDto
import com.dmitr.api.dto.DataResponseDto
import com.dmitr.api.entity.DataEntity
import com.dmitr.api.entity.UserEntity
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
        val dataEntity = data.toEntity(user)
        val hasEqualFilename = dataRepository.findByUserAndNameAndExtension(
            user,
            dataEntity.name,
            dataEntity.extension
        )

        if (hasEqualFilename) {
            throw FilenameEqualException()
        }

        return try {
            dataRepository.save(dataEntity)
        } catch (e: Exception) {
            throw FileUnsavedException()
        }.toResponseDto()
    }

    private fun DataRequestDto.toEntity(user: UserEntity) = DataEntity(
        name = fullName.substringBeforeLast("."),
        extension = fullName.substringAfterLast("."),
        length = blob.size.toLong(),
        blobData = blob,
        user = user
    )

    private fun DataEntity.toResponseDto() = (this as DataHeaderProjection)
        .toResponseDto()
        .copy(blob = blobData)

    private fun DataHeaderProjection.toResponseDto() = DataResponseDto(
        uuid = uuid,
        name = name,
        extension = extension,
        size = length
    )
}
