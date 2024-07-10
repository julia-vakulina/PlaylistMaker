package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDbConvertor {
    fun map(track: TrackFromAPI) :TrackEntity{
        return TrackEntity(track.id, track.artworkUrl100, track.trackName, track.artistName,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackTimeMillis, track.previewUrl!!
        )
    }
    fun map(track: TrackEntity) : TrackFromAPI {
        return TrackFromAPI(track.id, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }
}