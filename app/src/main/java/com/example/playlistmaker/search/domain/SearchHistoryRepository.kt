package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.TrackFromAPI

interface SearchHistoryRepository {
    //fun getSearchHistory() : SharedPreferences
    fun getHistory() : List<TrackFromAPI>
    fun putToHistory(trackFromAPI: TrackFromAPI) : Boolean
    fun clearHistory() : Boolean
}