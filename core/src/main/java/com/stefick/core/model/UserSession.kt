package com.stefick.core.model

data class UserSession (
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val token: String? = null
)