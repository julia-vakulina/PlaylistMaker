package com.example.playlistmaker.data

import android.content.Intent
import com.example.playlistmaker.domain.TrackGetter
import com.example.playlistmaker.domain.models.TrackFromAPI
import com.google.gson.Gson

class TrackGetterImpl: TrackGetter {
    override fun getTrack(key: String, intent: Intent): TrackFromAPI {
        val gson = Gson()
        val json = intent.getStringExtra(key)
        return gson.fromJson(json, TrackFromAPI::class.java)
    }

}