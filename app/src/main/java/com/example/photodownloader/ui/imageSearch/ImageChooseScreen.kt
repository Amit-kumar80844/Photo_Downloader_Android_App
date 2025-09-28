package com.example.photodownloader.ui.imageSearch


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Preview
@Composable
fun SearchResultsScreen() {
    Scaffold(
        topBar = { SearchBar(query = "Nature") }
    ) { innerPadding ->
        PhotoGrid(
            modifier = Modifier.padding(innerPadding),
            photos = samplePhotos()
        )
    }
}

@Composable
fun SearchBar(query: String) {
    var text by remember { mutableStateOf(query) }
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search images") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            singleLine = true
        )
    }
}

@Composable
fun PhotoGrid(modifier: Modifier = Modifier, photos: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(photos) { url ->
            PhotoCard(url = url)
        }
    }
}

@Composable
fun PhotoCard(url: String) {
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                if (isFavorite) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Unfavorite", tint = Color.Red)
                } else {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = Color.Gray)
                }
            }
            IconButton(
                onClick = { /* TODO: Download */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(Icons.Filled.Download, contentDescription = "Download", tint = Color.Gray)
            }
        }
    }
}

fun samplePhotos(): List<String> = listOf(
    "https://picsum.photos/id/1018/400/400",
    "https://picsum.photos/id/1025/400/400",
    "https://picsum.photos/id/1035/400/400",
    "https://picsum.photos/id/1043/400/400",
    "https://picsum.photos/id/1050/400/400",
    "https://picsum.photos/id/1062/400/400"
)
