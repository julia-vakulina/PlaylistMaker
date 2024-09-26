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

    override suspend fun getPlaylist(playlistId: Int): Playlist? {
        return playlistsRepository.getPlaylist(playlistId)
    }
    override suspend fun getTrackOfPlaylist(playlistId: Int): List<TrackFromAPI> {
        return playlistsRepository.getTrackOfPlaylist(playlistId)
    }

    override suspend fun getTotalDuration(playlistId: Int): Long {
        return playlistsRepository.getTotalDuration(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun sharePlaylist(playlistId: Int): Boolean {
        return playlistsRepository.sharePlaylist(playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist, trackList: List<TrackFromAPI>) {
        playlistsRepository.deletePlaylist(playlist, trackList)
    }

    override suspend fun updateNewPlaylist(playlist: Playlist) {
        playlistsRepository.updateNewPlaylist(playlist)
    }
}