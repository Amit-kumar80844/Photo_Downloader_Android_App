package com.example.photodownloader.domain.remote

import com.example.photodownloader.data.remote.APIResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    https://imagedownloader.duckdns.org/api/search?q=nature&image_type=all&order=popular&per_page=20&page=1&orientation=horizontal&category=backgrounds&min_width=800&min_height=600&colors=green&editors_choice=false&safesearch=true&mode=normal
    @GET("api/search")
    suspend fun getImages(
    @Query("q") query: String,
    @Query("image_type") imageType: String = "all",
    @Query("order") order: String = "popular",
    @Query("per_page") perPage: Int = 20,
    @Query("page") page: Int = 1,
    @Query("orientation") orientation: String? = null,
    @Query("category") category: String? = null,
    @Query("min_width") minWidth: Int? = null,
    @Query("min_height") minHeight: Int? = null,
    @Query("colors") colors: String? = null,
    @Query("editors_choice") editorsChoice: Boolean = false,
    @Query("safesearch") safeSearch: Boolean = true,
    @Query("mode") mode: String = "normal"
    ): APIResponse

//    https://imagedownloader.duckdns.org/api/download?imageUrl=https://cdn.pixabay.com/photo/2024/02/12/16/05/siguniang-mountain-8568913_1280.jpg

    @GET("api/download")
    suspend fun downloadImage(
        @Query("imageUrl") imageUrl: String
    ): ResponseBody
}