package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI

interface TrackInteractor {
    fun searchTrack (expression: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTracks: List<TrackFromAPI>?, errorMessage: String?)
    }
}