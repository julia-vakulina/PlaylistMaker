package com.example.playlistmaker.playlists.domain

sealed class AddState(val bottom: Boolean, val message: String) {
    class Success(message: String): AddState(true, message)
    class Error(message: String): AddState(false, message)
}