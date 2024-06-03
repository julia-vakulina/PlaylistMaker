package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI

class HistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository): HistoryInteractor {
    override fun getAllHistory(): List<TrackFromAPI> {
        return searchHistoryRepository.getHistory()
    }

    override fun putToHistory(trackFromAPI: TrackFromAPI): Boolean {
        searchHistoryRepository.putToHistory(trackFromAPI)
        return true
    }

    override fun clearHistory(): Boolean {
        searchHistoryRepository.clearHistory()
        return true
    }
}