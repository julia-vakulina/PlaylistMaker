package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.Resource

interface TrackRepository {
    fun searchTrack(expression: String) : Resource<List<TrackFromAPI>>
}