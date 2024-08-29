package com.example.playlistmaker.playlists.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel()
{
    private val playlistIsCreatedLiveData = MutableLiveData<Boolean>()
    fun getPlaylistIsCreatedLiveData(): LiveData<Boolean> = playlistIsCreatedLiveData
    fun savePlaylist(name: String, description: String, path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.insertPlaylist(
                Playlist(
                    playlistName = name,
                    playlistDescription = description,
                    tracksIds = ArrayList<Int>(),
                    pathToImage = path
                )
            )
        }
    }
    fun saveImageToAppStorage(uri: Uri, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.saveImageToAppStorage(uri, name)
        }
    }
    fun renderState() {
        playlistIsCreatedLiveData.postValue(true)
    }
}