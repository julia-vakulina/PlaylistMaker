package com.example.playlistmaker.presentation

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.PlayerInteractor
import java.util.Date
import java.util.Locale

class PlayerInteractorImpl: PlayerInteractor {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val DELAY = 500L
    }

    override var url: String? = null


    private val player = MediaPlayer()
    override var playerState = STATE_DEFAULT

    override fun preparePlayer(callback: () -> Unit) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        player.setOnCompletionListener {
            playerState = STATE_PREPARED
            callback()
        }
    }

    override fun startPlayer(callback: () -> Unit) {
        player.start()
        playerState = STATE_PLAYING
        callback()
    }

    override fun pausePlayer(callback: () -> Unit) {
        player.pause()
        playerState = STATE_PAUSED
        callback()
    }

    override fun playbackControl(callback1: () -> Unit, callback2: () -> Unit) {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer {callback1()}
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer {callback2()}
            }
        }
    }

    override fun release() {
        player.release()
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition
    }

   /*override fun startTimer() {
        mainThreadHandler = Handler(Looper.getMainLooper())
        mainThreadHandler?.post(updateTimer())
    }

    override fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }*/
}