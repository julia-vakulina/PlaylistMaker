package com.example.playlistmaker.player.ui

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    data class Playing (val progress: Long) : PlayerState()
    object Paused : PlayerState()
}