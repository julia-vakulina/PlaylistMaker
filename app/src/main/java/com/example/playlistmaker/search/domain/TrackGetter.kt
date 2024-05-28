package com.example.playlistmaker.search.domain

import android.content.Intent
import com.example.playlistmaker.player.domain.TrackFromAPI

interface TrackGetter {
    fun getTrack(key: String, intent: Intent) : TrackFromAPI
}