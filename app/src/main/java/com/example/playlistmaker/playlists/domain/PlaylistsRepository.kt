package com.example.playlistmaker.playlists.domain

import android.net.Uri
import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun insertPlaylist(playlist: Playlist)
    fun updatePlaylist(playlist: Playlist, track: TrackFromAPI) :Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun saveImageToAppStorage(uri: Uri, name: String): String
}