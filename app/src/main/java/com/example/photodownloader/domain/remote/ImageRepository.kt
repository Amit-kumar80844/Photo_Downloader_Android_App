package com.example.photodownloader.domain.remote

import com.example.photodownloader.data.remote.APIResponse
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Repository acts as a single point for fetching images or downloading them.
 * It hides Retrofit implementation details from the ViewModel.
 */
class ImageRepository @Inject constructor(
    private val apiService: ApiService
) {
    /**
     * Fetch images from remote API based on query and filters.
     */
    suspend fun getImages(
        query: String,
        imageType: String = "all",
        order: String = "popular",
        perPage: Int = 20,
        page: Int = 1,
        orientation: String? = null,
        category: String? = null,
        minWidth: Int? = null,
        minHeight: Int? = null,
        colors: String? = null,
        editorsChoice: Boolean = false,
        safeSearch: Boolean = true,
        mode: String = "normal"
    ): APIResponse {
        return apiService.getImages(
            query = query,
            imageType = imageType,
            order = order,
            perPage = perPage,
            page = page,
            orientation = orientation,
            category = category,
            minWidth = minWidth,
            minHeight = minHeight,
            colors = colors,
            editorsChoice = editorsChoice,
            safeSearch = safeSearch,
            mode = mode
        )
    }

    /**
     * Download image file from given image URL.
     * Returns raw binary data as ResponseBody.
     */
    suspend fun downloadImage(imageUrl: String): ResponseBody {
        return apiService.downloadImage(imageUrl)
    }
}
