package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override fun favoriteTracks(): Flow<List<TrackFromAPI>> {
        return favoritesRepository.selectFavorites()
    }

    // override fun favoriteTracksIds(): Flow<List<Long>> {
    //     return favoritesRepository.selectFavoritesIds()
    // }
    override  fun favoriteTracksIds(): List<Long> {
        return favoritesRepository.selectFavoritesIds()
    }

    override suspend fun insertTrackToFavorites(track: TrackFromAPI) {
        return favoritesRepository.insertTrackToFavorites(track)
    }

    override suspend fun deleteTrackFromFavorites(track: TrackFromAPI) {
        return favoritesRepository.deleteTrackFromFavorites(track)
    }
}