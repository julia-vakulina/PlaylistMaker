package com.example.playlistmaker.player.ui

import com.example.playlistmaker.playlists.domain.Playlist

sealed interface BottomSheetState {
    data object Loading: BottomSheetState
    data class Content(val playlists: List<Playlist>): BottomSheetState
}