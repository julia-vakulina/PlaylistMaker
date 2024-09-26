package com.example.playlistmaker.playlistItem.ui

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist

sealed interface PlaylistItemState {
    data object Loading : PlaylistItemState
    data class Content(
        val playlist: Playlist,
        val duration: String,
        val trackList: List<TrackFromAPI>
    ) : PlaylistItemState

    data class EmptyShare(val toastTextId: Int) : PlaylistItemState
}