package com.example.playlistmaker.domain

import java.util.Date

interface PlayerInteractor {
    var url: String?
    var playerState: Int
    fun preparePlayer(callback: () -> Unit)
    fun startPlayer(callback: () -> Unit)
    fun pausePlayer(callback: () -> Unit)
    fun playbackControl(callback1: () -> Unit, callback2: () -> Unit)
    fun release()
    fun getCurrentPosition() : Int
    /*fun startTimer()
    fun updateTimer()*/
}