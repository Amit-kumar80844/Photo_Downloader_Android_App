package com.example.photodownloader.ui.imageSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.photodownloader.data.remote.Hit

@Composable
fun ImageChooseScreen(
    uiState: ImageUiState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier.padding(5.dp, 10.dp)
            ) {
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
                PhotoStaggeredGrid(
                    photos = uiState.currentImages,
                    onImageClick = { hit, index ->
                        onEvent(UiEvent.OnImageClick(hit, index))
                    }
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
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
fun PhotoStaggeredGrid(
    photos: List<Hit>,
    onImageClick: (Hit, Int) -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(12.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(photos) { index, hit ->
            PhotoCard(
                hit = hit,
                onClick = { onImageClick(hit, index) }
            )
        }
    }
}

@Composable
fun PhotoCard(
    hit: Hit,
    onClick: () -> Unit
) {
    // Calculate aspect ratio from preview dimensions
    val aspectRatio = hit.previewWidth.toFloat() / hit.previewHeight.toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Use webformatURL for better quality in grid
            Image(
                painter = rememberAsyncImagePainter(
                    model = hit.webformatURL,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/400")
                ),
                contentDescription = hit.tags,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )

            // Gradient overlay at bottom for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Image info at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                // Tags
                Text(
                    text = hit.tags,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // User and stats
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = hit.user,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Likes
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Likes",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = formatNumber(hit.likes),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Views
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            Icons.Default.RemoveRedEye,
                            contentDescription = "Views",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = formatNumber(hit.views),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

// Helper function to format large numbers
private fun formatNumber(num: Int): String {
    return when {
        num >= 1_000_000 -> String.format("%.1fM", num / 1_000_000.0)
        num >= 1_000 -> String.format("%.1fK", num / 1_000.0)
        else -> num.toString()
    }
}