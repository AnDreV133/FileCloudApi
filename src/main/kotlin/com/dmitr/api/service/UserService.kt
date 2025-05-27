package com.dmitr.api.service

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import com.dmitr.api.dto.TokenPairResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.entity.SubscriptionLevelEnum
import com.dmitr.api.entity.TokenPairEntity
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.exception.UserAvailableException
import com.dmitr.api.exception.UserNotFoundException
import com.dmitr.api.repository.TokenPairRepository
import com.dmitr.api.repository.UserRepository
import com.dmitr.api.util.CustomUserDetails
import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenPairRepository: TokenPairRepository,
    private val jwtRefreshService: JwtRefreshService,
    private val jwtAccessService: JwtAccessService,
) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = username
            .let { userRepository.findByLogin(it) }
            ?: throw UsernameNotFoundException("Username $username not found")

        return CustomUserDetails(user)
    }

    @Transactional
    fun createUser(user: UserAuthDto): TokenPairResponseDto { // todo: can be parallel
        if (userRepository.findByLogin(user.login) != null) {
            throw UserAvailableException("User with login ${user.login} already exists")
        }

        var newUser = UserEntity(
            name = user.login,
            login = user.login,
            password = user.password,
            subscriptionLevel = SubscriptionLevelEnum.STANDARD,
        )

        newUser = userRepository.save(newUser)

        val newTokenPair = getTokenPair(newUser)

        return TokenPairResponseDto(
            tokenRefresh = newTokenPair.tokenRefresh,
            tokenAccess = newTokenPair.tokenAccess,
        )
    }

    @Transactional
    fun updateUser(user: UserAuthDto): TokenPairResponseDto {
        val userFromDb = userRepository.findByLogin(user.login)
            ?: throw UserNotFoundException("User with login ${user.login} not found")

        val newTokenPair = getTokenPair(userFromDb)

        return TokenPairResponseDto(
            tokenRefresh = newTokenPair.tokenRefresh,
            tokenAccess = newTokenPair.tokenAccess,
        )
    }

    private fun <T> getTokenPair(user: T) where T : ILogin, T : ISubscriptionLevel =
        TokenPairEntity(
            tokenRefresh = jwtRefreshService.generateToken(),
            tokenAccess = jwtAccessService.generateToken(user),
        )
}