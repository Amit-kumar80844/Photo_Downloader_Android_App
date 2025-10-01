package com.example.photodownloader.ui.imageSearch

import com.example.photodownloader.domain.remote.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class State{
    object ImageSearch: State()// ideal screen with landing and search bar
    data class ImageDetail(val imageUrl: String): State()// image detail screen with url
    data class ImageChoose(val images: List<List<String>>): State()//list of images to choose
}

sealed class ImageDetailEvent{
    data class DownloadImage(val url: String): ImageDetailEvent()
    data class SetAsWallpaper(val url: String): ImageDetailEvent()
    data class ShareImage(val url: String): ImageDetailEvent()
    object GoBack: ImageDetailEvent()
}

sealed class ImageChooseEvent{
    object Search: ImageChooseEvent()
    object LoadMore: ImageChooseEvent()
    object GoBack: ImageChooseEvent()
}

sealed class ImageSearchEvent{
    object Search: ImageSearchEvent()
    object GoToHistory: ImageSearchEvent()
    object GoToSettings: ImageSearchEvent()
    object GoToDownloads: ImageSearchEvent()
//    type state will be in viewmodel to make search better
}


@HiltViewModel
class ImageScreenViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) {
    // ViewModel implementation goes here
}
