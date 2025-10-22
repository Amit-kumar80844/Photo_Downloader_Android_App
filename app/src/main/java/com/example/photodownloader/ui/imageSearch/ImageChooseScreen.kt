package com.example.photodownloader.ui.imageSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageChooseScreen(
    uiState: ImageUiState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.padding(5.dp,10.dp)
            ){
                CommonSearchBar(
                    query = uiState.currentSearchQuery,
                    isLoading = uiState.isSearching,
                    onQueryChange = { onEvent(UiEvent.OnSearchQueryChange(it)) },
                    onSearch = { onEvent(UiEvent.OnSearchSubmit(uiState.currentSearchQuery)) },
                    onBackPress = { onEvent(UiEvent.OnBackPress) },
                    showBackButton = true
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.currentImages.isEmpty() && !uiState.isLoading) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "No images found",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Try a different search query",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                PhotoGrid(
                    photos = uiState.currentImages,
                    onImageClick = { url, index ->
                        onEvent(UiEvent.OnImageClick(url, index))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageChooseScreenPreview() {
    ImageChooseScreen(
        uiState = ImageUiState(
            currentSearchQuery = "Kittens",
            currentImages = listOf(
                "https://via.placeholder.com/400/FF0000/FFFFFF?Text=Image1",
                "https://via.placeholder.com/400/00FF00/000000?Text=Image2",
                "https://via.placeholder.com/400/0000FF/FFFFFF?Text=Image3",
                "https://via.placeholder.com/400/FFFF00/000000?Text=Image4",
                "https://via.placeholder.com/400/FF00FF/FFFFFF?Text=Image5",
                "https://via.placeholder.com/400/00FFFF/000000?Text=Image6"
            ),
            isSearching = false
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ImageChooseScreenEmptyPreview() {
    ImageChooseScreen(
        uiState = ImageUiState(
            currentSearchQuery = "Something that does not exist",
            currentImages = emptyList(),
            isSearching = false
        ),
        onEvent = {}
    )
}


@Composable
fun PhotoGrid(
    photos: List<String>,
    onImageClick: (String, Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(photos) { index, url ->
            PhotoCard(
                url = url,
                onClick = { onImageClick(url, index) }
            )
        }
    }
}

@Composable
fun PhotoCard(
    url: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = url,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/400")
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}