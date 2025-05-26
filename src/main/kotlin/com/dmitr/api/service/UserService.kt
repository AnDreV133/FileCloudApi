package com.dmitr.api.service

import com.dmitr.api.dto.TokenSessionResponseDto
import com.dmitr.api.dto.UserAuthDto
import com.dmitr.api.entity.SubscriptionLevelEnum
import com.dmitr.api.entity.TokenPairEntity
import com.dmitr.api.entity.UserEntity
import com.dmitr.api.exception.UserAvailableException
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
    override fun loadUserByUsername(username: String?): UserDetails {
        username ?: throw UsernameNotFoundException("Username is null")

        val user = username
            .let { userRepository.findByName(it) }
            ?: throw UsernameNotFoundException("Username $username not found")

        return CustomUserDetails(user)
    }

    @Transactional
    fun createUser(user: UserAuthDto): TokenSessionResponseDto { // todo: can be parallel
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

        val tokenRefresh = jwtRefreshService.generateToken()
        val tokenAccess = jwtAccessService.generateToken(newUser)

        val newTokenPair = TokenPairEntity(
            tokenRefresh = tokenRefresh,
            tokenAccess = tokenAccess,
        )

        return TokenSessionResponseDto(
            tokenRefresh = newTokenPair.tokenRefresh,
            tokenAccess = newTokenPair.tokenAccess,
        )
    }


}