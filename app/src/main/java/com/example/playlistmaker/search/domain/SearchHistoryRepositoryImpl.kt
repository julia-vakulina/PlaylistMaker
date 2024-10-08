package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.SearchHistory


class SearchHistoryRepositoryImpl(context: Context,
                                  private val appDatabase: AppDatabase): SearchHistoryRepository {
    val sharedPreferences: SharedPreferences

    val history: SearchHistory
    init {
        sharedPreferences = context.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
        history = SearchHistory(sharedPreferences)
    }
    override fun getHistory(): List<TrackFromAPI> {

        val favoriteTracksIds = appDatabase.trackDao().getIdTracks()

        val historyTracks = history.getTracks()
        historyTracks.forEach {
            if (it.trackId in favoriteTracksIds) {
                it.isFavorite = true
            }
        }
        return historyTracks
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