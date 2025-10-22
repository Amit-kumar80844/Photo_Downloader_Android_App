package com.example.photodownloader.ui.imageSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    imageUrl: String,
    uiState: ImageUiState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Image Details",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(UiEvent.OnBackPress) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Preview
            ImagePreview(url = imageUrl)

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtons(
                onDownload = { onEvent(UiEvent.OnDownloadImage(imageUrl)) },
                onShare = { onEvent(UiEvent.OnShareImage(imageUrl)) },
                onSetWallpaper = { onEvent(UiEvent.OnSetWallpaper(imageUrl)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            ImageInfoSection(imageUrl = imageUrl)
        }
    }
}

@Composable
fun ImagePreview(url: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = url,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/800x600")
                ),
                contentDescription = "Selected image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ActionButtons(
    onDownload: () -> Unit,
    onShare: () -> Unit,
    onSetWallpaper: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Download and Share buttons in a row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onDownload,
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(
                    Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Download")
            }

            OutlinedButton(
                onClick = { onShare() },
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Share")
            }
        }

        // Set Wallpaper button
        Button(
            onClick = onSetWallpaper,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Icon(
                Icons.Default.Wallpaper,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Set as Wallpaper")
        }
    }
}

@Composable
fun ImageInfoSection(imageUrl: String) {
    Text(
        text = "Image URL: $imageUrl",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ImageDetailScreenPreview() {
    // Use a placeholder image URL for the preview
    val sampleImageUrl = "https://via.placeholder.com/800x600"
    ImageDetailScreen(
        imageUrl = sampleImageUrl,
        uiState = ImageUiState(), // Assuming a default constructor
        onEvent = {} // No-op for preview
    )
}