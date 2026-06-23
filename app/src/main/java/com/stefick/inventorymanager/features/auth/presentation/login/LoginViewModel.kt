package com.stefick.inventorymanager.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefick.core.data.repository.AuthRepository
import com.stefick.core.model.AuthResult
import com.stefick.inventorymanager.features.auth.presentation.login.LoginContract.LoginEffect
import com.stefick.inventorymanager.features.auth.presentation.login.LoginContract.LoginEvent
import com.stefick.inventorymanager.features.auth.presentation.login.LoginContract.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Channels garantem que o evento (como navegação) só é entregue 1 vez à UI
    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> {
                _state.update { it.copy(emailInput = event.email, errorMessage = null) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.update { it.copy(passwordInput = event.password, errorMessage = null) }
            }
            is LoginEvent.OnErrorDismissed -> {
                _state.update { it.copy(errorMessage = null) }
            }
            LoginEvent.OnLoginClicked -> {
                performLogin()
            }
        }
    }

    private fun performLogin() {
        val currentState = _state.value
        if (currentState.emailInput.isBlank() || currentState.passwordInput.isBlank()) {
            _state.update { it.copy(errorMessage = "Preencha todos os campos.") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authRepository.login(
                email = currentState.emailInput.trim(),
                passwordRaw = currentState.passwordInput
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    // Envia a ordem de navegação para a UI
                    _effect.send(LoginEffect.NavigateToHome)
                }
                is AuthResult.InvalidCredentials -> {
                    _state.update { it.copy(errorMessage = "Email ou palavra-passe incorretos.") }
                }
                is AuthResult.Failure -> {
                    _effect.send(LoginEffect.ShowToast(result.message))
                }
                else -> Unit
            }
        }
    }
}