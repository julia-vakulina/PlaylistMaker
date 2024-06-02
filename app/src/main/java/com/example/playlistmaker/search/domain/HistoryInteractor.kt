package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI

interface HistoryInteractor {
    fun getAllHistory(): List<TrackFromAPI>
    fun putToHistory(trackFromAPI: TrackFromAPI): Boolean
    fun clearHistory(): Boolean
}