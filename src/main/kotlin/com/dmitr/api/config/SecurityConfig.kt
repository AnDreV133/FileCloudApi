package com.dmitr.api.config

import com.dmitr.api.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private var userService: UserService,
    private var jwtRequestFilter: JwtRequestFilter,
    private val exceptionHandlingFilter: ExceptionHandlingFilter,
) {
    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests {
            it.requestMatchers("$contextPath/auth/**").permitAll()
            it.anyRequest().authenticated()
        }
        .httpBasic(withDefaults())
        .addFilterBefore(exceptionHandlingFilter, LogoutFilter::class.java)
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder())
            setUserDetailsService(userService)
        }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}