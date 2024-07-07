package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String) : Flow<Resource<List<TrackFromAPI>>>
}