package com.example.photodownloader.ui.imageSearch

import androidx.collection.mutableIntSetOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photodownloader.data.local.PreviousSearch
import com.example.photodownloader.data.remote.APIResponse
import com.example.photodownloader.data.remote.Hit
import com.example.photodownloader.domain.remote.ImageRepository
import com.example.photodownloader.domain.room.PhotoDownloaderDao
import com.example.photodownloader.ui.imageSearch.Screen.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/* -----------------------------------------------------------
   SCREEN NAVIGATION STATES
   ----------------------------------------------------------- */
sealed class Screen {
    object ImageSearch : Screen()
    data class ImageChoose(val searchQuery: String) : Screen()
    data class ImageDetail(val hit: Hit, val imageIndex: Int) : Screen()
}

/* -----------------------------------------------------------
   UI STATE
   ----------------------------------------------------------- */
data class ImageUiState(
    val screen: Screen = ImageSearch,
    val currentSearchQuery: String = "",
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
    val suggestions: List<String> = listOf(
        "Backgrounds", "Fashion", "Nature", "Science", "Education",
        "Feelings", "Religion", "Places", "Animals", "Sports", "Buildings"
    ),
    val previousSearches: List<PreviousSearch> = emptyList(),
    var imageResponseMap: Map<Int, APIResponse> = emptyMap(),
    val currentImages: List<Hit> = emptyList()
)

/* -----------------------------------------------------------
   UI EVENTS
----------------------------------------------------------- */

sealed class UiEvent {
    data class OnSearchQueryChange(val query: String) : UiEvent()
    data class OnSearchSubmit(val query: String) : UiEvent()
    data class OnSuggestionClick(val suggestion: String) : UiEvent()
    data class OnPreviousSearchClick(val search: PreviousSearch) : UiEvent()
    data class OnDeletePreviousSearch(val searchId: Int) : UiEvent()
    data class OnImageClick(val hit: Hit, val imageIndex: Int) : UiEvent()
    object OnBackPress : UiEvent()
    data class OnDownloadImage(val url: String) : UiEvent()
    data class OnShareImage(val url: String) : UiEvent()
    data class OnSetWallpaper(val url: String) : UiEvent()
    object OnNavigateToSettings : UiEvent()
    object OnNavigateToDownloads : UiEvent()
    object ClearError : UiEvent()
    object OnClearImage : UiEvent()
}

/* -----------------------------------------------------------
   VIEWMODEL
   ----------------------------------------------------------- */
@HiltViewModel
class ImageScreenViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val photoDownloaderDao: PhotoDownloaderDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    var choosenImageHit: Pair<Int, Hit?> by mutableStateOf(0 to null)
    var previousSearchQuery:String by mutableStateOf("")

    init {
        observePreviousSearches()
    }
    var imageIndexCounter: Int by mutableIntStateOf(0)

    /**
     * Main event handler
     */
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnSearchQueryChange -> {
              /*  _uiState.update { it.copy(imageResponseMap = emptyMap()) }
                imageIndexCounter =0;*/
               /* _uiState.update {
                    it.copy(
                        currentImages = emptyList()
                    )
                }*/
                _uiState.update { it.copy(currentSearchQuery = event.query) }
            }

            is UiEvent.OnSearchSubmit -> {
                if (event.query.isNotBlank()) {
                    performSearch(event.query.trim())
                }
            }

            is UiEvent.OnSuggestionClick -> {
                performSearch(event.suggestion)
            }

            is UiEvent.OnPreviousSearchClick -> {
                performSearch(event.search.previousQuery)
            }

            is UiEvent.OnDeletePreviousSearch -> {
                deleteSearch(event.searchId)
            }

            is UiEvent.OnImageClick -> {
                choosenImageHit = Pair(event.imageIndex,event.hit)
                _uiState.update {
                    it.copy(screen = ImageDetail(event.hit, event.imageIndex))
                }
            }

            UiEvent.OnBackPress -> {
                handleBackPress()
            }

            is UiEvent.OnDownloadImage -> {
                downloadImage(event.url)
            }

            is UiEvent.OnShareImage -> {
                // Handle sharing - will be implemented in UI layer
            }

            is UiEvent.OnSetWallpaper -> {
                // Handle wallpaper - will be implemented in UI layer
            }

            UiEvent.OnNavigateToSettings -> {
                // Navigate to settings - implement navigation here
            }

            UiEvent.OnNavigateToDownloads -> {
                // Navigate to downloads - implement navigation here
            }

            UiEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }

            UiEvent.OnClearImage -> {
                imageIndexCounter = 0
                _uiState.update {
                    it.copy(
                        imageResponseMap = emptyMap(),
                        currentImages = emptyList()
                    )
                }
            }
        }
    }

    /**
     * Perform image search
     */
    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                if(previousSearchQuery!=query){
                    _uiState.update {
                        it.copy(
                            currentImages = emptyList()
                        )
                    }
                }
                previousSearchQuery = query
                _uiState.update {
                    it.copy(
                        isSearching = true,
                        isLoading = true,
                        currentSearchQuery = query,
                        errorMessage = null
                    )
                }

                // Save search to history
                saveSearchToHistory(query)
                // Fetch images from repository
                val response = imageRepository.getImages(query = query)

                // Store response in the map and extract hits
                val allImages = _uiState.value.currentImages + response.hits

                _uiState.update {
                    it.copy(
                        screen = ImageChoose(query),
                        imageResponseMap = it.imageResponseMap.plus(Pair(imageIndexCounter++, response)),
                        currentImages = allImages,
                        isSearching = false,
                        isLoading = false
                    )
                }

                println("Search completed. Total images: ${allImages.size}")
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        isLoading = false,
                        errorMessage = "Failed to search images: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * Handle back press based on current screen
     */
    private fun handleBackPress() {
        val currentScreen = _uiState.value.screen
        when (currentScreen) {
            is ImageDetail -> {
                // Go back to image choose screen
                _uiState.update {
                    it.copy(screen = ImageChoose(it.currentSearchQuery))
                }
            }

            is ImageChoose -> {
                // Go back to search screen
                _uiState.update {
                    it.copy(
                        screen = ImageSearch,
                        currentImages = emptyList()
                    )
                }
            }

            ImageSearch -> {
                // Already at root, do nothing or exit app
            }
        }
    }

    /**
     * Download image
     */
    private fun downloadImage(url: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val imageData = imageRepository.downloadImage(url)
                // TODO: Save to file storage

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to download image: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * Observe previous searches from database
     */
    private fun observePreviousSearches() {
        viewModelScope.launch {
            photoDownloaderDao.getAllPreviousSearches().collectLatest { searches ->
                _uiState.update { it.copy(previousSearches = searches) }
            }
        }
    }

    /**
     * Save search query to history
     */
    private fun saveSearchToHistory(query: String) {
        viewModelScope.launch {
            try {
                val search = PreviousSearch(
                    previousQuery = query
                )
                photoDownloaderDao.insertPreviousSearch(search)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Delete search from history
     */
    private fun deleteSearch(id: Int) {
        viewModelScope.launch {
            try {
                photoDownloaderDao.deletePreviousSearchById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}