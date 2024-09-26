package com.example.playlistmaker.playlists.domain

import android.net.Uri
import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun insertPlaylist(playlist: Playlist)
    fun updatePlaylist(playlist: Playlist, track: TrackFromAPI) :Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun saveImageToAppStorage(uri: Uri, name: String): String
   suspend fun getPlaylist(playlistId: Int): Playlist?
    suspend fun getTrackOfPlaylist(playlistId: Int): List<TrackFromAPI>
    suspend fun getTotalDuration(playlistId: Int): Long
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)
    suspend fun sharePlaylist(playlistId: Int): Boolean
    suspend fun deletePlaylist(playlist: Playlist, trackList: List<TrackFromAPI>)
    suspend fun updateNewPlaylist(playlist: Playlist)
}