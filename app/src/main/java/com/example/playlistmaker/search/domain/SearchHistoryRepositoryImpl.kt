package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.SearchHistory


class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {
    val sharedPreferences: SharedPreferences
    //override fun getSearchHistory(): SharedPreferences {
    //    return sharedPreferences
    //}
    val history: SearchHistory
    init {
        sharedPreferences = context.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
        history = SearchHistory(sharedPreferences)
    }
    override fun getHistory(): ArrayList<TrackFromAPI> {
        history.getTracks()
    }

    override fun putToHistory(trackFromAPI: TrackFromAPI): Boolean {

    }
    companion object {
        private const val HISTORY_KEY = "history"
    }
}