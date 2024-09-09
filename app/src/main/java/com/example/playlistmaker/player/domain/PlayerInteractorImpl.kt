package com.example.playlistmaker.player.domain

import android.media.MediaPlayer
import android.util.Log

class PlayerInteractorImpl(private val player: MediaPlayer): PlayerInteractor {
    companion object {
         const val STATE_DEFAULT = 0
         const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
         const val STATE_PAUSED = 3
    }

    override var url: String? = null

    override var playerState = STATE_DEFAULT

    override fun isPlaying(): Boolean = player.isPlaying


    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)  {
        with (player) {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                onPrepared.invoke()
                playerState= STATE_PREPARED
            }
            setOnCompletionListener {
                onCompletion.invoke()
                seekTo(0)
                playerState= STATE_PREPARED
            }
        }
    }

    override fun startPlayer() {
        player.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun release() {
        player.release()
    }

    override fun reset() {
        player.reset()
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition
    }

}