package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.TrackFromAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {
    private val _favoriteTracks = MutableLiveData<FavoritesState>()
    val favoriteTracks: LiveData<FavoritesState> = _favoriteTracks
    fun fillData(){
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.favoriteTracks().collect {tracks ->
                processResult(tracks)
            }
        }
    }
    private fun processResult(tracks: List<TrackFromAPI>) {
        if (tracks.isEmpty()) {
            renderState(FavoritesState.Empty("no favorite tracks"))
        } else {
            renderState(FavoritesState.Content(tracks))
        }
    }

    private fun renderState(state: FavoritesState) {
        _favoriteTracks.postValue(state)
    }
}