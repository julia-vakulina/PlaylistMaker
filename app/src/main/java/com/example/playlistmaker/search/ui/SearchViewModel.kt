package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.TrackInteractor

class SearchViewModel(private val getTrackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor) : ViewModel() {
    private var loadingLiveData = MutableLiveData(false)
    private var placeholderLiveData = MutableLiveData("")
    private var tracksLiveData = MutableLiveData(ArrayList<TrackFromAPI>())
    private var tracksHistoryLiveData = MutableLiveData(ArrayList<TrackFromAPI>())
    private var searchHistoryLiveData = MutableLiveData(false)
    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun getPlaceholderLiveData(): LiveData<String> = placeholderLiveData
    fun getTracksLiveData(): LiveData<ArrayList<TrackFromAPI>> = tracksLiveData
    fun getTracksHistoryLiveData() : LiveData<ArrayList<TrackFromAPI>> = tracksHistoryLiveData
    fun getSearchHistoryLiveData(): LiveData<Boolean> = searchHistoryLiveData

    private val handler = Handler(Looper.getMainLooper())
    val tracks = ArrayList<TrackFromAPI>()
    var historyTracks = ArrayList<TrackFromAPI>()
    fun getHistory() {
        historyTracks = historyInteractor.getAllHistory() as ArrayList<TrackFromAPI>
        resetHistory()
    }
    fun putToHistory(trackFromAPI: TrackFromAPI) {
        historyInteractor.putToHistory(trackFromAPI = trackFromAPI)
    }

    fun searchTrack(text: String) {
        loadingLiveData.postValue(true)
        if (text.isNotEmpty()) {
            getTrackInteractor.searchTrack(text, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<TrackFromAPI>?, errorMessage: String?) {
                    handler.post {
                        loadingLiveData.postValue(false)
                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            tracksLiveData.postValue(foundTracks as ArrayList<TrackFromAPI>?)
                        }
                        if (tracks.isEmpty()) {
                            if (errorMessage.equals( "no internet")) {
                                placeholderLiveData.postValue("no internet")
                            } else {
                                placeholderLiveData.postValue("not found")
                            }
                            }
                    }
                }
            })
        }
    }
    fun resetHistory() {
        tracksHistoryLiveData.postValue(historyTracks)
    }
    fun searchHistoryVisible(visible: Boolean) {
        searchHistoryLiveData.postValue(visible)
    }
    fun clear() {
        tracksLiveData.postValue(ArrayList<TrackFromAPI>())
    }
    fun clearHistory() {
        historyTracks = ArrayList<TrackFromAPI>()
        historyInteractor.clearHistory()
    }
    fun searchDebounce(searchRunnable: Runnable) {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}