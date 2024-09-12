package com.example.playlistmaker.playlists.ui

import com.example.playlistmaker.playlists.domain.Playlist

sealed interface EditPlaylistState {
    data class PlaylistInfo(val playlist: Playlist) : EditPlaylistState
}