package com.example.playlistmaker.media.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val playlistLiveData = MutableLiveData<PlaylistScreenState>()
     fun getPlaylistLiveData(): LiveData<PlaylistScreenState> = playlistLiveData
    init {
        getPlaylists()
    }
     fun getPlaylists() {
        renderState(PlaylistScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }
    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistScreenState.Empty)
        } else {
            renderState(PlaylistScreenState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistScreenState) {
        playlistLiveData.postValue(state)
    }
}