package com.example.photodownloader.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.photodownloader.data.local.Setting

@Composable
fun SettingScreen(
    navController: NavHostController
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is SettingUiState.Idle -> {
                SettingsContent(
                    state = settings,
                    onSave = { setting ->
                        viewModel.saveSetting(setting)
                    },
                    onReset = {
                        viewModel.saveDefaultSetting()
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            is SettingUiState.Error -> {
                ErrorView(message = (uiState as SettingUiState.Error).message)
            }

            is SettingUiState.Loading -> {
                LoadingView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    state: Setting,
    onSave: (Setting) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    var safeSearch by remember(state.safeSearch) { mutableStateOf(state.safeSearch) }
    var onlyEditorsChoice by remember(state.onlyEditorsChoice) { mutableStateOf(state.onlyEditorsChoice) }
    var minHeight by remember(state.minHeight) { mutableIntStateOf(state.minHeight) }
    var minWidth by remember(state.minWidth) { mutableIntStateOf(state.minWidth) }
    var perPage by remember(state.perPage) { mutableIntStateOf(state.perPage) }

    // Update local state when settings change
    LaunchedEffect(state) {
        safeSearch = state.safeSearch
        onlyEditorsChoice = state.onlyEditorsChoice
        minHeight = state.minHeight
        minWidth = state.minWidth
        perPage = state.perPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onSave(
                        Setting(
                            id = 1,
                            safeSearch = safeSearch,
                            onlyEditorsChoice = onlyEditorsChoice,
                            minHeight = minHeight,
                            minWidth = minWidth,
                            perPage = perPage
                        )
                    )
                }
            ) {
                Icon(Icons.Default.Save, "Save")
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text("General", style = MaterialTheme.typography.titleMedium)

            SettingToggle("Safe Search", safeSearch) { safeSearch = it }
            SettingToggle("Only Editor's Choice", onlyEditorsChoice) { onlyEditorsChoice = it }

            Spacer(Modifier.height(16.dp))

            Text("Image Filters", style = MaterialTheme.typography.titleMedium)

            SettingSlider("Min Height", minHeight, 0..4000) { minHeight = it }
            SettingSlider("Min Width", minWidth, 0..4000) { minWidth = it }
            SettingSlider("Images Per Page", perPage, 3..200) { perPage = it }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset to Default")
            }
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Int,
    range: IntRange,
    onChange: (Int) -> Unit
) {
    Text("$label: $value", Modifier.padding(top = 8.dp))
    Slider(
        value = value.toFloat(),
        onValueChange = { onChange(it.toInt()) },
        valueRange = range.first.toFloat()..range.last.toFloat()
    )
}

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text(message, color = MaterialTheme.colorScheme.error)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsContent(
        state = Setting(
            1,
            safeSearch = true,
            onlyEditorsChoice = false,
            minHeight = 500,
            minWidth = 500,
            perPage = 50
        ),
        onSave = {},
        onReset = {},
        onBack = {}
    )
}