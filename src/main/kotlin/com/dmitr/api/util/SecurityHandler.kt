package com.dmitr.api.util

import org.springframework.security.core.context.SecurityContextHolder

inline fun getUserLoginFromSecurityContext(): String = SecurityContextHolder.getContext().authentication.name