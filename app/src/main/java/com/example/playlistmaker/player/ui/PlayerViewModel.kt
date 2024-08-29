package com.example.playlistmaker.player.ui

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.AddState
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private lateinit var track: TrackFromAPI
    private var timerLiveData = MutableLiveData(0)
    private var playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    private var timerJob: Job? = null
    private var isFavoriteLiveData = MutableLiveData<Boolean?>(null)
    private var bottomSheetLiveData = MutableLiveData<BottomSheetState>()
    private var addTrackToPlaylistLiveData = MutableLiveData<AddState>()

    fun getIsFavoriteLiveData(): LiveData<Boolean?> = isFavoriteLiveData
    fun getTimerLiveData() : LiveData<Int> = timerLiveData
    fun getPlayerStateLiveData() : LiveData<PlayerState> = playerStateLiveData
    fun getBottomSheetLiveData(): LiveData<BottomSheetState> = bottomSheetLiveData
    fun getAddTrackToPlaylistLiveData(): LiveData<AddState> = addTrackToPlaylistLiveData
    init {
        getBottomSheet()
    }
    fun setupTrack(trackFromAPI: TrackFromAPI) {
        track = trackFromAPI
        viewModelScope.launch {
            val isTrackFavorite = favoritesInteractor.isTrackFavorite(trackFromAPI.trackId)
            track.isFavorite = isTrackFavorite
            isFavoriteLiveData.postValue(isTrackFavorite)
        }
    }
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
    fun addTrackToPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val isAdd = playlistInteractor.updatePlaylist(playlist, track)
            if (isAdd) {
                addTrackToPlaylistLiveData.postValue(AddState.Success("Добавлено в плейлист " + playlist.playlistName))
            } else {
                addTrackToPlaylistLiveData.postValue(AddState.Error("Трек уже добавлен в плейлист " + playlist.playlistName))
            }
        }
    }
    fun getBottomSheet() {
        bottomSheetLiveData.postValue(BottomSheetState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { playlist ->
                bottomSheetLiveData.postValue(BottomSheetState.Content(playlist))
            }
        }
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