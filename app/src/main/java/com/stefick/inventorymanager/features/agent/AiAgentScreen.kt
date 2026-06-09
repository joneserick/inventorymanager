package com.stefick.inventorymanager.features.agent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stefick.ai.agent.AiAgentEvent
import com.stefick.ai.agent.AiAgentUiState
import com.stefick.ai.tts.awaitSpeak
import com.stefick.ai.tts.rememberSpeechRecognizer
import com.stefick.ai.tts.rememberTextToSpeech
import com.stefick.inventorymanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAgentScreen(
    viewModel: AiAgentViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val tts = rememberTextToSpeech()

    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
        }
    }

    val speechRecognizer = rememberSpeechRecognizer(
        onResult = { text -> viewModel.onEvent(AiAgentEvent.OnSpeechRecognized(text)) },
        onError = { error -> viewModel.onEvent(AiAgentEvent.OnSpeechError(error)) },
        onEndOfSpeech = {
            //TODO add animation
        }
    )

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasAudioPermission = isGranted }
    )

    LaunchedEffect(uiState) {
        if (uiState is AiAgentUiState.Speaking) {
            val textToSpeak = (uiState as AiAgentUiState.Speaking).message
            tts?.awaitSpeak(textToSpeak)
            viewModel.onEvent(AiAgentEvent.StopSpeaking)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.inventory_assistent)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack,
                        stringResource(R.string.back)
                    ) }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val (icon, tint, statusText) = when (uiState) {
                is AiAgentUiState.Idle -> Triple(Icons.Default.Mic, Color.Gray,
                    stringResource(R.string.agent_state)
                )
                is AiAgentUiState.Listening -> Triple(
                    Icons.Default.MicNone,
                    Color.Red,
                    stringResource(R.string.i_am_listening)
                )

                is AiAgentUiState.Processing -> Triple(
                    Icons.Default.Sync,
                    Color.Blue,
                    stringResource(R.string.processing)
                )

                is AiAgentUiState.Speaking -> Triple(
                    Icons.Default.VolumeUp,
                    Color.Green,
                    stringResource(R.string.answering)
                )

                is AiAgentUiState.Error -> Triple(
                    Icons.Default.Error,
                    Color.Red,
                    (uiState as AiAgentUiState.Error).message
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.agent_state),
                tint = tint,
                modifier = Modifier
                    .size(120.dp)
                    .clickable(enabled = uiState is AiAgentUiState.Idle) {
                        if (hasAudioPermission) {
                            viewModel.onEvent(AiAgentEvent.StartListening)
                            speechRecognizer.startListening(speechIntent)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = statusText, style = MaterialTheme.typography.titleLarge)

            if (uiState is AiAgentUiState.Speaking) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (uiState as AiAgentUiState.Speaking).message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}