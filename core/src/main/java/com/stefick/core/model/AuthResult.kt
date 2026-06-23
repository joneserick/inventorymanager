package com.stefick.core.model

sealed interface AuthResult {
    data class Success(val session: UserSession) : AuthResult
    object InvalidCredentials : AuthResult
    data class Failure(val message: String) : AuthResult
}