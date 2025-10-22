package com.example.photodownloader.domain.remote

import com.example.photodownloader.data.remote.APIResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ApiService defines all network endpoints for the Photo Downloader app.
 * Retrofit automatically implements this interface at runtime.
 */
interface ApiService {

    /* ------------------------------------------------------------
       🔹 SEARCH IMAGES ENDPOINT
       Example:
       https://imagedownloader.duckdns.org/api/search?q=nature&image_type=all&order=popular...
       ------------------------------------------------------------ */

    @GET("api/search")
    suspend fun getImages(
        /** Search keyword (e.g. “nature”, “mountains”, etc.) */
        @Query("q") query: String,

        /** Type of images to include: "all", "photo", "illustration", etc. */
        @Query("image_type") imageType: String = "all",

        /** Order of results: "popular" or "latest" */
        @Query("order") order: String = "popular",

        /** Number of results per page */
        @Query("per_page") perPage: Int = 20,

        /** Page number for pagination */
        @Query("page") page: Int = 1,

        /** Optional orientation: "horizontal" or "vertical" */
        @Query("orientation") orientation: String? = null,

        /** Optional category (e.g. "backgrounds", "nature", "fashion") */
        @Query("category") category: String? = null,

        /** Minimum image width (pixels) */
        @Query("min_width") minWidth: Int? = null,

        /** Minimum image height (pixels) */
        @Query("min_height") minHeight: Int? = null,

        /** Filter by color theme (e.g. "green", "blue") */
        @Query("colors") colors: String? = null,

        /** Whether to show only editor’s choice images */
        @Query("editors_choice") editorsChoice: Boolean = false,

        /** Whether to filter adult content */
        @Query("safesearch") safeSearch: Boolean = true,

        /** Mode of search (e.g. "normal") */
        @Query("mode") mode: String = "normal"
    ): APIResponse


    /* ------------------------------------------------------------
       🔹 DOWNLOAD IMAGE ENDPOINT
       Example:
       https://imagedownloader.duckdns.org/api/download?imageUrl=https://cdn.pixabay.com/photo.jpg
       ------------------------------------------------------------ */
    @GET("api/download")
    suspend fun downloadImage(
        /** Full image URL you want to download */
        @Query("imageUrl") imageUrl: String
    ): ResponseBody
}
