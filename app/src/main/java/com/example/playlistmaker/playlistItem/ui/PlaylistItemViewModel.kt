package com.example.playlistmaker.playlistItem.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistInteractor
import com.example.playlistmaker.playlists.domain.PlaylistInteractorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class PlaylistItemViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val dateFormat = SimpleDateFormat("mm", Locale.getDefault())
    private val screenStateLiveData = MutableLiveData<PlaylistItemState>()
    fun getScreenStateLiveData() : LiveData<PlaylistItemState> = screenStateLiveData
    init {
        getScreenState(playlistId)
    }
    fun getScreenState(playlistId: Int) {
        screenStateLiveData.postValue(PlaylistItemState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = getPlaylist(playlistId)
            if (playlist != null) {
                val trackList = getTrackList(playlistId)
                val duration = getTotalDurationInMinutes(playlistId, trackList)
                screenStateLiveData.postValue(
                    PlaylistItemState.Content(playlist, duration, trackList)
                )
            }
        }
    }
    fun deleteTrack(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                playlistInteractor.deleteTrackFromPlaylist(playlistId, trackId)
            }.join()
            getScreenState(playlistId)
        }
    }
    fun sharePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val isShare = playlistInteractor.sharePlaylist(playlistId)
            if (!isShare) screenStateLiveData.postValue(
                PlaylistItemState.EmptyShare(
                    R.string.empty_share
                )
            )
        }
    }
    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val trackList = getTrackList(playlistId)
            playlistInteractor.deletePlaylist(playlist, trackList)
        }
    }
    private suspend fun getPlaylist(playlistId: Int): Playlist? {
        return playlistInteractor.getPlaylist(playlistId)
    }
    private suspend fun getTrackList(playlistId: Int): List<TrackFromAPI> {
        return playlistInteractor.getTrackOfPlaylist(playlistId)
    }
    private suspend fun getTotalDurationInMinutes(playlistId: Int, trackList: List<TrackFromAPI>): String {
        var duration = 0L
        if (trackList.isNotEmpty()) duration = playlistInteractor.getTotalDuration(playlistId)
        return dateFormat.format(duration)
    }

}