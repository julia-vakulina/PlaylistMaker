package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var timerLiveData = MutableLiveData(0)
    private var playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    private var timerJob: Job? = null

    fun getTimerLiveData() : LiveData<Int> = timerLiveData
    fun getPlayerStateLiveData() : LiveData<PlayerState> = playerStateLiveData
    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(PROGRESS_DELAY)
                timerLiveData.postValue(playerInteractor.getCurrentPosition())
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
    companion object {
        const val PROGRESS_DELAY = 300L
    }
}