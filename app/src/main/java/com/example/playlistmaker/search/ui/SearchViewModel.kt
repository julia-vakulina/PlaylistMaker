package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.TrackInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val getTrackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor,
    private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    private var loadingLiveData = MutableLiveData(false)
    private var placeholderLiveData = MutableLiveData("")
    private var tracksLiveData = MutableLiveData(ArrayList<TrackFromAPI>())
    private var tracksHistoryLiveData = MutableLiveData(ArrayList<TrackFromAPI>())
    private var searchHistoryLiveData = MutableLiveData(false)
    private var tracksListLiveData = MutableLiveData(false)
    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun getPlaceholderLiveData(): LiveData<String> = placeholderLiveData
    fun getTracksLiveData(): LiveData<ArrayList<TrackFromAPI>> = tracksLiveData
    fun getTracksHistoryLiveData() : LiveData<ArrayList<TrackFromAPI>> = tracksHistoryLiveData
    fun getSearchHistoryLiveData(): LiveData<Boolean> = searchHistoryLiveData
    fun getTracksListVisibilityLiveData(): LiveData<Boolean> = tracksListLiveData

    private var searchJob: Job? = null
    var historyTracks = ArrayList<TrackFromAPI>()
    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
        historyTracks = historyInteractor.getAllHistory() as ArrayList<TrackFromAPI>
        resetHistory()
        }
    }
    fun putToHistory(trackFromAPI: TrackFromAPI) {
        historyInteractor.putToHistory(trackFromAPI = trackFromAPI)
    }

    fun searchTrack(text: String) {
        loadingLiveData.postValue(true)
        if (text.isNotEmpty()) {
            viewModelScope.launch {
                getTrackInteractor
                    .searchTrack(text)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }
    private fun processResult(foundTracks: List<TrackFromAPI>?, errorMessage: String?) {
        loadingLiveData.postValue(false)
        val tracks = mutableListOf<TrackFromAPI>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
            tracksLiveData.postValue(foundTracks as ArrayList<TrackFromAPI>?)
        }
        if (tracks.isEmpty()){
            if (errorMessage.equals("no internet")) {
                placeholderLiveData.postValue("no internet")
            } else {
                placeholderLiveData.postValue("not found")
            }
        }
    }
    fun resetHistory() {
        tracksHistoryLiveData.postValue(historyTracks)
    }
    fun searchHistoryVisible(visible: Boolean) {
        searchHistoryLiveData.postValue(visible)
    }
    fun tracksListVisible(visible: Boolean) {
        tracksListLiveData.postValue(visible)
    }
    fun clear() {
        tracksLiveData.postValue(ArrayList<TrackFromAPI>())
    }
    fun clearHistory() {
        historyTracks = ArrayList<TrackFromAPI>()
        historyInteractor.clearHistory()
    }
    fun searchDebounce(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(text)
        }
    }

    fun markTrackFavorite(track: TrackFromAPI) {
        val favoriteTracks = favoritesInteractor.favoriteTracksIds()
        if (track.trackId in favoriteTracks) track.isFavorite = true
        else track.isFavorite = false
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}