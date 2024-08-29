package com.example.playlistmaker.media.ui

import com.example.playlistmaker.playlists.domain.Playlist

sealed interface PlaylistScreenState {
    data object Empty: PlaylistScreenState
    data object Loading: PlaylistScreenState
    data class Content(val playlists: List<Playlist>): PlaylistScreenState
}