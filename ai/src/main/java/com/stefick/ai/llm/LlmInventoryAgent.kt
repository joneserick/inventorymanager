package com.stefick.ai.llm

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.stefick.ai.R
import com.stefick.ai.llm.AgentActions.ADD
import com.stefick.ai.llm.AgentActions.CHECK
import com.stefick.ai.llm.AgentActions.REMOVE
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

const val MAX_TOKENS = 512
const val INFERENCE_TEMPERATURE = 0.2f

@Singleton
class LlmInventoryAgent @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var llmInference: LlmInference? = null

    init {
        val modelPath = File(context.filesDir, "gemma-2b-it-cpu-int4.bin").absolutePath

        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(MAX_TOKENS)
            .setTemperature(INFERENCE_TEMPERATURE)
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
    }

    fun executeCommand(userInput: String): AgentResponse {
        val systemPrompt = """
            You are a strict inventory management assistant. Analyze the user input and reply ONLY in this exact format:
            ACTION|[QUANTITY]|[PRODUCT]|[VOICE_RESPONSE]
            
            Rules:
            1. ACTION must be ADD, REMOVE, CHECK, or UNKNOWN.
            2. QUANTITY must be an integer. If not specified, use 1.
            3. PRODUCT must be the extracted name of the item.
            4. VOICE_RESPONSE must be a natural sentence answering the user in their language.
            5. If the user asks about non-inventory topics (like weather, jokes, etc), set ACTION to UNKNOWN and VOICE_RESPONSE to a polite refusal.
            
            Example 1: "adicionar 5 pacotes de arroz" -> ADD|5|Arroz|Perfeito, adicionei 5 unidades de arroz ao estoque.
            Example 2: "quanto café eu tenho?" -> CHECK|1|Café|Vou verificar a quantidade de café para si.
            Example 3: "como está o tempo hoje?" -> UNKNOWN|0||Desculpe, apenas consigo ajudar com tarefas do inventário.
        """.trimIndent()

        val fullPrompt = "$systemPrompt\n\nUser: $userInput\nAssistant:"

        val rawResult = llmInference?.generateResponse(fullPrompt) ?: return AgentResponse.Unknown(
            context.getString(
                R.string.error_on_ai_engine
            )
        )

        return parseLlmOutput(rawResult)
    }

    private fun parseLlmOutput(output: String): AgentResponse {
        return try {
            val parts = output.trim().split("|")
            val action = parts[0]
            val quantity = parts[1].toIntOrNull() ?: 1
            val product = parts[2]
            val voiceMessage = parts[3]

            when (action) {
                ADD.name -> AgentResponse.Add(quantity, product, voiceMessage)
                REMOVE.name -> AgentResponse.Remove(quantity, product, voiceMessage)
                CHECK.name -> AgentResponse.Check(product, voiceMessage)
                else -> AgentResponse.Unknown(voiceMessage)
            }
        } catch (e: Exception) {
            AgentResponse.Unknown(context.getString(R.string.could_not_process_the_command))
        }
    }
}

sealed class AgentResponse {
    data class Add(val quantity: Int, val product: String, val voiceMessage: String) :
        AgentResponse()

    data class Remove(val quantity: Int, val product: String, val voiceMessage: String) :
        AgentResponse()

    data class Check(val product: String, val voiceMessage: String) : AgentResponse()
    data class Unknown(val voiceMessage: String) : AgentResponse()
}