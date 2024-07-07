package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TracksResponse
}