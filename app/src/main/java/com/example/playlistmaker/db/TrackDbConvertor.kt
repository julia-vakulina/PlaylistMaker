package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI

class TrackDbConvertor {
    fun map(track: TrackFromAPI) :TrackEntity{
        return TrackEntity(track.trackId, track.artworkUrl100, track.trackName, track.artistName,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackTimeMillis, track.previewUrl!!
        )
    }
    fun map(track: TrackEntity) : TrackFromAPI {
        return TrackFromAPI(track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, isFavorite = true)
    }
}