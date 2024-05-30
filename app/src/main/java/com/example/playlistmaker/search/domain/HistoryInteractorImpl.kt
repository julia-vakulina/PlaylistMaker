package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI

class HistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository): HistoryInteractor {
    override fun getAllHistory(): ArrayList<TrackFromAPI> {
        se
    }

    override fun putToHistory(trackFromAPI: TrackFromAPI): Boolean {

    }
}