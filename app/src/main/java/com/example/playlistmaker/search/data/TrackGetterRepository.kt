package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.google.gson.Gson

class TrackGetterRepository {
    fun getTrack(json: String) : TrackFromAPI {
        val gson = Gson()
        return gson.fromJson(json, TrackFromAPI::class.java)
    }
}