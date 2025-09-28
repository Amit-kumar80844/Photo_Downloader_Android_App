package com.example.photodownloader.ui.imageSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter



@Preview
@Composable
fun ImageDetailsScreen() {
    Scaffold(
        topBar = { DetailsHeader() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ImagePreview(
                url = "https://picsum.photos/id/1018/800/600"
            )
            ActionButtons()
            RelatedImagesGrid(
                images = sampleRelatedImages()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsHeader() {
    TopAppBar(
        title = { Text("Image Details", style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            IconButton(onClick = { /* TODO: Back */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun ImagePreview(url: String) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
fun ActionButtons() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /* TODO: Download */ },
                modifier = Modifier.weight(1f),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Download")
            }
            OutlinedButton(
                onClick = { /* TODO: Share */ },
                modifier = Modifier.weight(1f),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share")
            }
        }
        OutlinedButton(
            onClick = { /* TODO: Set as Wallpaper */ },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Wallpaper, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Set as Wallpaper")
        }
    }
}

@Composable
fun RelatedImagesGrid(images: List<String>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Related Images",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { /* TODO: Open details */ }
                )
            }
        }
    }
}

fun sampleRelatedImages(): List<String> = listOf(
    "https://picsum.photos/id/1025/400/400",
    "https://picsum.photos/id/1035/400/400",
    "https://picsum.photos/id/1043/400/400",
    "https://picsum.photos/id/1050/400/400",
    "https://picsum.photos/id/1062/400/400",
    "https://picsum.photos/id/1074/400/400"
)


