package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoriteTracks(): Flow<List<TrackFromAPI>>
    fun favoriteTracksIds(): Flow<List<Long>>
    suspend fun insertTrackToFavorites(track: TrackFromAPI)
    suspend fun deleteTrackFromFavorites(track: TrackFromAPI)
}