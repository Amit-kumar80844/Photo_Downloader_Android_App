package com.example.photodownloader.domain.remote

import com.example.photodownloader.data.remote.APIResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getImages(query: String): APIResponse {
        return apiService.getImages(query = query)
    }

    suspend fun downloadImage(imageUrl: String): ResponseBody {
        return apiService.downloadImage(imageUrl)
    }
}
