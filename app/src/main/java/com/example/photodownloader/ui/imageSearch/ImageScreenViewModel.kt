package com.example.photodownloader.ui.imageSearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.photodownloader.domain.remote.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class State {
    object ImageSearch : State()// ideal screen with landing and search bar
    data class ImageDetail(val imageUrl: String) : State()// image detail screen with url
    data class ImageChoose(val images: List<List<String>>) : State()//list of images to choose
}

sealed class ImageDetailEvent {
    object Idle : ImageDetailEvent() // idle state
    data class DownloadImage(val url: String) : ImageDetailEvent()
    data class SetAsWallpaper(val url: String) : ImageDetailEvent()
    data class ShareImage(val url: String) : ImageDetailEvent()
    object GoBack : ImageDetailEvent()
}

sealed class ImageChooseEvent {
    object Idle : ImageChooseEvent()
    object Search : ImageChooseEvent()
    object LoadMore : ImageChooseEvent()
    object GoBack : ImageChooseEvent()
}

sealed class ImageSearchEvent {
    object Idle : ImageSearchEvent()
    object Search : ImageSearchEvent()
    object GoToSettings : ImageSearchEvent()
    object GoToDownloads : ImageSearchEvent()
//    type state will be in viewmodel to make search better
}

@HiltViewModel
class ImageScreenViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    var state by mutableStateOf<State>(State.ImageSearch)
        private set

    var imageChooseState by mutableStateOf<ImageChooseEvent>(ImageChooseEvent.Idle)
        private set

    var imageDetailState by mutableStateOf<ImageDetailEvent>(ImageDetailEvent.Idle)
        private set

    var imageSearchState by mutableStateOf<ImageSearchEvent>(ImageSearchEvent.Idle)
        private set

    // all states in one place
    var searchHistory by mutableStateOf<List<String>>(emptyList())
    val suggestion by mutableStateOf(
        listOf(
            "Backgrounds",
            "Fashion", "Nature", "Science", "Education",
            "Feelings", "Religion", "Places", "Animals", "Sports", "Buildings"
        )
    )

}
