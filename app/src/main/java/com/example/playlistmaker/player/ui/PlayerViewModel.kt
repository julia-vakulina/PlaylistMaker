package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerInteractor

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    //companion object {
     //   fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
     //       initializer {

      //          PlayerViewModel(
      //              playerInteractor = Creator.providePlayerInteractor()
       //         )
       //     }
       // }
       // var isPrepared = false

    //}



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
        //if (!isPrepared) {
            playerInteractor.preparePlayer(url,
                onPrepared = {
                renderState(PlayerState.Prepared)
            }, onCompletion = {
                renderState(PlayerState.Prepared)
                }
            )
            //isPrepared = true
        //}
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