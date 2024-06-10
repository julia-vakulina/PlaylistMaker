package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerInteractor

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {


    private var timerLiveData = MutableLiveData(0)
    private var playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)


    fun getTimerLiveData() : LiveData<Int> = timerLiveData
    fun getPlayerStateLiveData() : LiveData<PlayerState> = playerStateLiveData
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    fun startTimer() {
        mainThreadHandler.post(updateTimer())

    }
    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                playerStateLiveData.postValue(PlayerState.Playing(playerInteractor.getCurrentPosition().toLong()))
                    timerLiveData.postValue(playerInteractor.getCurrentPosition())
                    mainThreadHandler.postDelayed(this, PlayerInteractorImpl.DELAY)
            }
        }
    }

    fun preparePlayer(url: String) {
            playerInteractor.preparePlayer(url,
                onPrepared = {
                renderState(PlayerState.Prepared)
            }, onCompletion = {
                renderState(PlayerState.Prepared)
                }
            )
    }
    fun renderState(playerState: PlayerState) {
        playerStateLiveData.postValue(playerState)
    }

    fun playbackControl() {
        playerInteractor.playbackControl()
    }

    fun isPlaying() : Boolean {
        return playerInteractor.isPlaying()
    }
    fun onPause() {
        playerInteractor.pausePlayer()
    }
    override fun onCleared() {
        super.onCleared()
        playerInteractor.reset()
        playerStateLiveData.postValue(PlayerState.Default)
    }
}