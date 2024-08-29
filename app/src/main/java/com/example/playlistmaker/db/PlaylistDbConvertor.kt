package com.example.playlistmaker.db

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlistName = playlist.playlistName,
           playlistDescription =  playlist.playlistDescription,
           tracksIds =  gson.fromJson(playlist.tracksIds, object : TypeToken<ArrayList<Int>>() {}.type),
           pathToImage =  playlist.pathToImage,
           numberOfTracks =  playlist.numberOfTracks)
    }
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(id = 0,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            tracksIds = gson.toJson(playlist.tracksIds),
            pathToImage = playlist.pathToImage,
            numberOfTracks = playlist.numberOfTracks)
    }
    fun idsToJson(ids: ArrayList<Int>): String {
        return gson.toJson(ids)
    }
    fun map(track: TrackFromAPI): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl!!
        )
    }
}