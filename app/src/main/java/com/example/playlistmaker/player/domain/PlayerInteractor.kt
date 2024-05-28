package com.example.playlistmaker.player.domain

import java.util.Date

interface PlayerInteractor {
    var url: String?
    var playerState: Int
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun isPlaying() : Boolean
    fun release()
    fun reset()
    fun getCurrentPosition() : Int
}