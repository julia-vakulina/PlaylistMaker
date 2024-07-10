package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override fun favoriteTracks(): Flow<List<TrackFromAPI>> {
        return favoritesRepository.selectFavorites()
    }
}