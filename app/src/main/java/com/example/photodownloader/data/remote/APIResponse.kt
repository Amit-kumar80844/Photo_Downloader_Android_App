package com.example.photodownloader.data.remote

data class APIResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)