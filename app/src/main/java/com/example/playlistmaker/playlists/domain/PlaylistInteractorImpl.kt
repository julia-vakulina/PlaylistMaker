package com.example.playlistmaker.playlists.domain

import android.net.Uri
import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
): PlaylistInteractor {
    override fun insertPlaylist(playlist: Playlist) {
        playlistsRepository.insertPlaylist(playlist)
    }

    override fun updatePlaylist(playlist: Playlist, track: TrackFromAPI) : Boolean {
        return playlistsRepository.updatePlaylist(playlist, track)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun saveImageToAppStorage(uri: Uri, name: String): String {
        return playlistsRepository.saveImageToAppStorage(uri, name)
    }
}