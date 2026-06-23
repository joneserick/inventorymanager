package com.stefick.inventorymanager.features.auth.presentation.login

interface LoginContract {
    data class LoginState(
        val emailInput: String = "",
        val passwordInput: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface LoginEvent {
        data class OnEmailChanged(val email: String) : LoginEvent
        data class OnPasswordChanged(val password: String) : LoginEvent
        object OnLoginClicked : LoginEvent
        object OnErrorDismissed : LoginEvent
    }

    sealed interface LoginEffect {
        object NavigateToHome : LoginEffect
        data class ShowToast(val message: String) : LoginEffect
    }
}