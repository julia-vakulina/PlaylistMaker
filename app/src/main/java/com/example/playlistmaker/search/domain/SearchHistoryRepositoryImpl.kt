package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.SearchHistory


class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {
    val sharedPreferences: SharedPreferences

    val history: SearchHistory
    init {
        sharedPreferences = context.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
        history = SearchHistory(sharedPreferences)
    }
    override fun getHistory(): List<TrackFromAPI> {
        return history.getTracks()
    }

    override fun putToHistory(trackFromAPI: TrackFromAPI): Boolean {
        history.addTrack(trackFromAPI)
        return true
    }

    override fun clearHistory(): Boolean {
        history.clear()
        return true
    }
    companion object {
        private const val HISTORY_KEY = "history"
    }
}