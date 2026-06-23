package com.stefick.ai.agent

sealed class AiAgentUiState {
    object Idle : AiAgentUiState()
    object Listening : AiAgentUiState()
    object Processing : AiAgentUiState()
    data class Speaking(val message: String) : AiAgentUiState()
    data class Error(val message: String) : AiAgentUiState()
}

sealed class AiAgentEvent {
    object StartListening : AiAgentEvent()
    data class OnSpeechRecognized(val text: String) : AiAgentEvent()
    data class OnSpeechError(val error: String) : AiAgentEvent()
    object StopSpeaking : AiAgentEvent()
}

sealed class AgentIntent {
    data class Add(val quantity: Int, val product: String) : AgentIntent()
    data class Remove(val quantity: Int, val product: String) : AgentIntent()
    data class Check(val product: String) : AgentIntent()
    object Unknown : AgentIntent()
}