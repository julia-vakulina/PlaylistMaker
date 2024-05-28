package com.example.playlistmaker.search.data

import android.content.Intent
import com.example.playlistmaker.search.domain.TrackGetter
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.google.gson.Gson

class TrackGetterImpl: TrackGetter {
    override fun getTrack(key: String, intent: Intent): TrackFromAPI {
        val gson = Gson()
        val json = intent.getStringExtra(key)
        return gson.fromJson(json, TrackFromAPI::class.java)
    }

}