package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {

    override fun selectFavorites(): Flow<List<TrackFromAPI>> {
        return appDatabase.trackDao().getTracks().map { convertFromTrackEntity(it) }
    }

    override  fun selectFavoritesIds(): List<Long> {
        val trackIds = appDatabase.trackDao().getIdTracks()
        return trackIds
    }

    override suspend fun insertTrackToFavorites(track: TrackFromAPI) {
        val trackEntity = convertFromTrack(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromFavorites(track: TrackFromAPI) {
        val trackEntity = convertFromTrack(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override suspend fun isTrackFavorite(id: Long): Boolean {
        return appDatabase.trackDao().isTrackLiked(id)
    }
    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackFromAPI> {
        return tracks.map {track -> trackDbConvertor.map(track) }
    }
    private fun convertFromTrack(track: TrackFromAPI) : TrackEntity {
        return  trackDbConvertor.map(track)
    }
}