package com.example.photodownloader.ui.imageSearch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.photodownloader.data.local.PreviousSearch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ImageScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button press
    BackHandler(enabled = uiState.screen !is Screen.ImageSearch) {
        viewModel.onEvent(UiEvent.OnBackPress)
    }

    // Show error snackbar if there's an error
    if (uiState.errorMessage != null) {
        LaunchedEffect(uiState.errorMessage) {
            // Show snackbar or toast
            // Then clear error
            viewModel.onEvent(UiEvent.ClearError)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = uiState.screen,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { it }
                ) + fadeIn(tween(300)) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { -it }
                        ) + fadeOut(tween(300))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                Screen.ImageSearch -> {
                    ImageSearchScreen(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                }

                is Screen.ImageChoose -> {
                    ImageChooseScreen(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                }

                is Screen.ImageDetail -> {
                    ImageDetailScreen(
                        hit = viewModel.choosenImageHit.second!!,
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }

        // Global loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ImageSearchScreen(
    uiState: ImageUiState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        topBar = { SearchScreenHeader() },
        bottomBar = {
            BottomNavBar(
                currentScreen = "Search",
                onNavigateToSettings = { onEvent(UiEvent.OnNavigateToSettings) },
                onNavigateToDownloads = { onEvent(UiEvent.OnNavigateToDownloads) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                CommonSearchBar(
                    query = uiState.currentSearchQuery,
                    isLoading = uiState.isSearching,
                    onQueryChange = { onEvent(UiEvent.OnSearchQueryChange(it)) },
                    onSearch = { onEvent(UiEvent.OnSearchSubmit(uiState.currentSearchQuery)) },
                    showBackButton = false
                )
            }

            item {
                SuggestedSection(
                    suggestions = uiState.suggestions,
                    onSuggestionClick = { onEvent(UiEvent.OnSuggestionClick(it)) }
                )
            }

            item {
                if (uiState.previousSearches.isNotEmpty()) {
                    SearchHistorySection(
                        searches = uiState.previousSearches,
                        onSearchClick = { onEvent(UiEvent.OnPreviousSearchClick(it)) },
                        onDeleteClick = { onEvent(UiEvent.OnDeletePreviousSearch(it)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageSearchScreenPreview() {
    val sampleUiState = ImageUiState(
        suggestions = listOf("Nature", "City", "Abstract", "Food"),
        previousSearches = listOf(
            PreviousSearch(id = 1, previousQuery = "Sunsets"),
            PreviousSearch(id = 2, previousQuery = "Mountain landscapes"),
            PreviousSearch(id = 3, previousQuery = "Ocean waves")
        ),
        currentSearchQuery = "preview search",
        isSearching = false
    )
    ImageSearchScreen(
        uiState = sampleUiState,
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ImageSearchScreenLoadingPreview() {
    val sampleUiState = ImageUiState(
        suggestions = listOf("Nature", "City", "Abstract", "Food"),
        previousSearches = emptyList(),
        currentSearchQuery = "loading state",
        isSearching = true
    )
    ImageSearchScreen(
        uiState = sampleUiState,
        onEvent = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenHeader() {
    TopAppBar(
        title = {
            Text(
                "Photo Downloader",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    )
}

@Composable
fun CommonSearchBar(
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBackPress: (() -> Unit)? = null,
    showBackButton: Boolean = false
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search for images") },
        leadingIcon = if (showBackButton && onBackPress != null) {
            {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        } else null,
        trailingIcon = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onSearch,
                    enabled = query.isNotBlank()
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun SuggestedSection(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    Column {
        Text(
            "Suggested",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { label ->
                AssistChip(
                    onClick = { onSuggestionClick(label) },
                    label = { Text(label) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        labelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun SearchHistorySection(
    searches: List<PreviousSearch>,
    onSearchClick: (PreviousSearch) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Column {
        Text(
            "Search History",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            searches.take(10).forEach { search ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSearchClick(search) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            search.previousQuery,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }
                    IconButton(onClick = { onDeleteClick(search.id) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentScreen: String,
    onNavigateToSettings: () -> Unit,
    onNavigateToDownloads: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == "Search",
            onClick = { /* Already on search screen */ },
            icon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            label = { Text("Search", fontSize = 12.sp) }
        )
        NavigationBarItem(
            selected = currentScreen == "Downloads",
            onClick = onNavigateToDownloads,
            icon = {
                Icon(Icons.Default.Download, contentDescription = "Downloads")
            },
            label = { Text("Downloads", fontSize = 12.sp) }
        )
        NavigationBarItem(
            selected = currentScreen == "Settings",
            onClick = onNavigateToSettings,
            icon = {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            },
            label = { Text("Settings", fontSize = 12.sp) }
        )
    }
}