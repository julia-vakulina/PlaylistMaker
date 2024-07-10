package com.example.playlistmaker.search.data

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.TrackDbConvertor
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor) : TrackRepository {
    override fun searchTrack(expression: String): Flow<Resource<List<TrackFromAPI>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("no internet"))
            }

            200 -> {
                with(response as TracksResponse){
                val data = results.map {
                    TrackFromAPI(
                        it.id,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                        )
                    }

                    val favoriteTracksIds = appDatabase.trackDao().getIdTracks()
                    markFavoriteTracks(data, favoriteTracksIds)

                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("server error"))
            }
        }
    }
    private suspend fun markFavoriteTracks(data: List<TrackFromAPI>, favoriteTracksIds: List<Long>) {
        data.forEach {
            if (it.id in favoriteTracksIds) {
                it.isFavorite = true
            }
        }
    }


}