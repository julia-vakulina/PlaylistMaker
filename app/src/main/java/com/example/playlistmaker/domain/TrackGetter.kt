package com.example.playlistmaker.domain

import android.content.Intent
import com.example.playlistmaker.domain.models.TrackFromAPI

interface TrackGetter {
    fun getTrack(key: String, intent: Intent) : TrackFromAPI
}