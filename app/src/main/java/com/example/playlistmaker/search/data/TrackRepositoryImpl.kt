package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.domain.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): Resource<List<TrackFromAPI>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("no internet")
            }

            200 -> {
                Resource.Success((response as TracksResponse).results.map {
                    TrackFromAPI(
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
                })
            }

            else -> {
                Resource.Error("server error")
            }
        }
    }

}