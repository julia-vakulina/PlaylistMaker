package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerInteractor

class PlayerInteractorImpl: PlayerInteractor {
    companion object {
         const val STATE_DEFAULT = 0
         const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
         const val STATE_PAUSED = 3
        const val DELAY = 500L
    }

    override var url: String? = null


    private val player = MediaPlayer()
    override var playerState = STATE_DEFAULT

    //override fun preparePlayer(callback: () -> Unit) {
    //    player.setDataSource(url)
    //    player.prepareAsync()
    //    player.setOnPreparedListener {
    //        //playerState = STATE_PREPARED
    //    }
    //    player.setOnCompletionListener {
    //        player.seekTo(0)
    //playerState = STATE_PREPARED
    //        callback()
    //    }
    //}
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
                seekTo(0)
                onCompletion.invoke()
                playerState= STATE_PREPARED
            }
        }
    }

    override fun startPlayer() {
        player.start()
        playerState = STATE_PLAYING
        //callback()
    }

    override fun pausePlayer() {
        player.pause()
        playerState = STATE_PAUSED
        //callback()
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