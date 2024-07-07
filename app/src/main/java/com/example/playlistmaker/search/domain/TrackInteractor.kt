package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack (expression: String): Flow<Pair<List<TrackFromAPI>?, String?>>
}