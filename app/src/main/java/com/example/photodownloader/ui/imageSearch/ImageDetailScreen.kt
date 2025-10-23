package com.example.photodownloader.ui.imageSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.photodownloader.data.remote.Hit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    hit: Hit,
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
            // High-quality Image Preview
            ImagePreview(url = hit.largeImageURL)

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtons(
                onDownload = { onEvent(UiEvent.OnDownloadImage(hit.largeImageURL)) },
                onShare = { onEvent(UiEvent.OnShareImage(hit.largeImageURL)) },
                onSetWallpaper = { onEvent(UiEvent.OnSetWallpaper(hit.largeImageURL)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Detailed Image Info
            ImageInfoSection(hit = hit)
        }
    }
}

@Composable
fun ImagePreview(url: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = url,
                placeholder = rememberAsyncImagePainter("https://via.placeholder.com/800x600"),
                contentDescription = "Selected image",
                error = rememberAsyncImagePainter("https://via.placeholder.com/800x600"),
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
                onClick = onShare,
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
fun ImageInfoSection(hit: Hit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = "Image Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Tags
            InfoRow(
                icon = Icons.AutoMirrored.Filled.Label,
                label = "Tags",
                value = hit.tags
            )

            // User
            InfoRow(
                icon = Icons.Default.Person,
                label = "Uploaded by",
                value = hit.user
            )

            // Dimensions
            InfoRow(
                icon = Icons.Default.PhotoSizeSelectLarge,
                label = "Dimensions",
                value = "${hit.imageWidth} × ${hit.imageHeight}"
            )

            // Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    icon = Icons.Default.Favorite,
                    label = "Likes",
                    value = formatNumber(hit.likes),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(
                    icon = Icons.Default.RemoveRedEye,
                    label = "Views",
                    value = formatNumber(hit.views),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    icon = Icons.Default.Download,
                    label = "Downloads",
                    value = formatNumber(hit.downloads),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(
                    icon = Icons.AutoMirrored.Filled.Comment,
                    label = "Comments",
                    value = formatNumber(hit.comments),
                    modifier = Modifier.weight(1f)
                )
            }

            // Image Type
            InfoRow(
                icon = Icons.Default.Category,
                label = "Type",
                value = hit.type.replaceFirstChar { it.uppercase() }
            )

            // File Size
            InfoRow(
                icon = Icons.Default.Storage,
                label = "File Size",
                value = formatFileSize(hit.imageSize)
            )

            // AI Generated Badge
            if (hit.isAiGenerated) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI Generated",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

// Helper function to format file size
private fun formatFileSize(bytes: Int): String {
    val bytesLong = bytes.toLong()
    return when {
        bytesLong >= 1_000_000_000 -> String.format("%.2f GB", bytesLong / 1_000_000_000.0)
        bytesLong >= 1_000_000 -> String.format("%.2f MB", bytesLong / 1_000_000.0)
        bytesLong >= 1_000 -> String.format("%.2f KB", bytesLong / 1_000.0)
        else -> "$bytesLong B"
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDetailScreenPreview() {
    // Mock data for preview
    val mockHit = Hit(
        id = 1,
        largeImageURL = "https://via.placeholder.com/800x600",
        tags = "nature, flower, beautiful",
        user = "testuser",
        imageWidth = 1920,
        imageHeight = 1080,
        likes = 1500,
        views = 250000,
        downloads = 750,
        comments = 120,
        type = "photo",
        imageSize = 4500000,
        isAiGenerated = true,
        collections = 0,
        isGRated = false,
        isLowQuality = false,
        noAiTraining = false,
        pageURL = "",
        previewHeight = 150,
        previewURL = "",
        previewWidth = 150,
        userImageURL = "",
        userURL = "",
        user_id = 1,
        webformatHeight = 480,
        webformatURL = "",
        webformatWidth = 640
    )
    ImageDetailScreen(hit = mockHit, uiState = ImageUiState(), onEvent = {})
}
