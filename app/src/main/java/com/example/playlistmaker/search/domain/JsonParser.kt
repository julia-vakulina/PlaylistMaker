package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI

interface JsonParser {
    fun <T> jsonToObject(json: String, classOfT: Class<T>): T
}