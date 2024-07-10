package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override fun selectFavorites(): Flow<List<TrackFromAPI>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun insertTrackToFavorites(track: TrackFromAPI) {
        val trackEntity = convertFromTrack(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromFavorites(track: TrackFromAPI) {
        val trackEntity = convertFromTrack(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }
    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackFromAPI> {
        return tracks.map {track -> trackDbConvertor.map(track) }
    }
    private fun convertFromTrack(track: TrackFromAPI) : TrackEntity {
        return  trackDbConvertor.map(track)
    }
}