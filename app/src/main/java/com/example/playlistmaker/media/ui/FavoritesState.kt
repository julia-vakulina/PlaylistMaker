package com.example.playlistmaker.media.ui

import com.example.playlistmaker.player.domain.TrackFromAPI

sealed interface FavoritesState {
    data class Content(val tracks: List<TrackFromAPI>) : FavoritesState
    data class Empty(val emptyMessage: String) : FavoritesState
}