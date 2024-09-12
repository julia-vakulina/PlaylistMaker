package com.example.playlistmaker.playlists.ui

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private var playlist: Playlist
    ) : PlaylistViewModel(playlistInteractor) {

    private val playlistInfoLiveData = MutableLiveData<EditPlaylistState>()
    fun getPlaylistInfoLiveData(): LiveData<EditPlaylistState> = playlistInfoLiveData
    init {
        getPlaylistInfo()
    }
    fun getPlaylistInfo() {
        playlistInfoLiveData.postValue(EditPlaylistState.PlaylistInfo(playlist))
    }
    fun updatePlaylist(playlistName:String, playlistDescription: String?, pathToImage: String?) {
        var path = playlist.pathToImage
        if (pathToImage != null && pathToImage != "") path = pathToImage

        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylist = Playlist(
                id = playlist.id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToImage = path,
                numberOfTracks = playlist.numberOfTracks
            )
            playlistInteractor.updateNewPlaylist(newPlaylist)
            playlist = newPlaylist
        }
    }

}