package com.stefick.inventorymanager.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefick.core.data.repository.AuthRepository
import com.stefick.core.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    // Extrai apenas o Role da sessão atual e expõe como StateFlow
    // O stateIn garante que o fluxo começa a ser lido imediatamente
    val userRole: StateFlow<UserRole?> = authRepository.currentSession
        .map { session -> session?.role }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}