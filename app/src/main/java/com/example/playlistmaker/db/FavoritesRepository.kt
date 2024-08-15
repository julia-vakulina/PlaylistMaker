package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun insertTrackToFavorites(track: TrackFromAPI)
    suspend fun deleteTrackFromFavorites(track: TrackFromAPI)
    fun selectFavorites(): Flow<List<TrackFromAPI>>
     fun selectFavoritesIds(): List<Long>
}