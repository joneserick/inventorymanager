package com.stefick.inventorymanager.features.agent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefick.ai.agent.AiAgentEvent
import com.stefick.ai.agent.AiAgentUiState
import com.stefick.ai.llm.AgentResponse
import com.stefick.ai.llm.LlmInventoryAgent
import com.stefick.core.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AiAgentViewModel @Inject constructor(
    private val repository: InventoryRepository,
    private val aiAgent: LlmInventoryAgent
) : ViewModel() {

    private val _uiState = MutableStateFlow<AiAgentUiState>(AiAgentUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AiAgentEvent) {
        when (event) {
            is AiAgentEvent.StartListening -> _uiState.value = AiAgentUiState.Listening
            is AiAgentEvent.OnSpeechRecognized -> processWithAiAgent(event.text)
            is AiAgentEvent.OnSpeechError -> _uiState.value = AiAgentUiState.Error(event.error)
            is AiAgentEvent.StopSpeaking -> _uiState.value = AiAgentUiState.Idle
        }
    }

    private fun processWithAiAgent(text: String) {
        _uiState.value = AiAgentUiState.Processing

        viewModelScope.launch(Dispatchers.Default) {
            when (val response = aiAgent.executeCommand(text)) {
                is AgentResponse.Add -> {
                    respond(response.voiceMessage)
                }

                is AgentResponse.Remove -> {
                    respond(response.voiceMessage)
                }

                is AgentResponse.Check -> {
                    respond(response.voiceMessage)
                }

                is AgentResponse.Unknown -> {
                    respond(response.voiceMessage)
                }
            }
        }
    }

    private fun respond(message: String) {
        _uiState.value = AiAgentUiState.Speaking(message)
    }
}