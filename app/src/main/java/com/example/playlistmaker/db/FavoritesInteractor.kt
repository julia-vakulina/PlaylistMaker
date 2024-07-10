package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoriteTracks(): Flow<List<TrackFromAPI>>
}