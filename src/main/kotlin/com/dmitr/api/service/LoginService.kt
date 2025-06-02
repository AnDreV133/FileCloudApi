package com.dmitr.api.service

import com.dmitr.api.declaration.ILogin
import com.dmitr.api.declaration.ISubscriptionLevel
import com.dmitr.api.dto.TokenPairResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.entity.SubscriptionLevelEnum
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.exception.TokenRefreshExpiredException
import com.dmitr.api.exception.UserAvailableException
import com.dmitr.api.exception.UserNotFoundException
import com.dmitr.api.repository.UserRepository
import com.dmitr.api.util.CustomUserDetails
import io.jsonwebtoken.JwtException
import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
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
            throw UserAvailableException(user.login)
        }

        val newUser = userRepository.save(
            UserEntity(
                name = user.login,
                login = user.login,
                password = user.password,
                subscriptionLevel = SubscriptionLevelEnum.STANDARD,
            )
        )

        val newTokenPair = getTokenPair(newUser)

        return TokenPairResponseDto(
            tokenRefresh = newTokenPair.tokenRefresh,
            tokenAccess = newTokenPair.tokenAccess,
        )
    }

    @Transactional
    fun updateUser(user: UserAuthDto): TokenPairResponseDto {
        val userFromDb = userRepository.findByLoginAndPassword(user.login, user.password)
            ?: throw UserNotFoundException(user.login)

        val newTokenPair = getTokenPair(userFromDb)

        return TokenPairResponseDto(
            tokenRefresh = newTokenPair.tokenRefresh,
            tokenAccess = newTokenPair.tokenAccess,
        )
    }

    @Transactional
    fun getNewAccessToken(refreshToken: String): String {
        val login = try {
            jwtService.getLoginFromAccessToken(refreshToken)
        } catch (e: JwtException) {
            throw TokenRefreshExpiredException()
        }

        val user = userRepository.findByLogin(login)
            ?: throw UserNotFoundException(login)

        return jwtService.generateAccessToken(user)
    }

    private fun <T> getTokenPair(user: T) where T : ILogin, T : ISubscriptionLevel =
        TokenPairResponseDto(
            tokenRefresh = jwtService.generateRefreshToken(user),
            tokenAccess = jwtService.generateAccessToken(user),
        )
}