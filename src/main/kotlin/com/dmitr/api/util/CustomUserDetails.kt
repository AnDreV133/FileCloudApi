package com.dmitr.api.util

import com.dmitr.api.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: UserEntity) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
        SimpleGrantedAuthority("USER")
    )

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.login
}