package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private var timerLiveData = MutableLiveData(0)
    private var playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    private var timerJob: Job? = null
    private var isFavoriteLiveData = MutableLiveData<Boolean?>(null)

    fun getIsFavoriteLiveData(): LiveData<Boolean?> = isFavoriteLiveData
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
    fun onFavoriteClicked(track: TrackFromAPI) {
        viewModelScope.launch(Dispatchers.IO) {
            val newFavoriteStatus = if (!track.isFavorite) {
                favoritesInteractor.insertTrackToFavorites(track)
                true
            } else {
                favoritesInteractor.deleteTrackFromFavorites(track)
                false
            }
            isFavoriteLiveData.postValue(newFavoriteStatus)
            track.isFavorite = newFavoriteStatus
        }
    }
    suspend fun isTrackFavorite(trackId: Long): Boolean {
        val favoriteTracks: Flow<List<Long>> = favoritesInteractor.favoriteTracksIds()

        val favoriteTracksIds: MutableList<Long> = mutableListOf()

        favoriteTracks.collect { list ->
            favoriteTracksIds.addAll(list)
        }
        return favoriteTracksIds.contains(trackId)
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