package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.SharedPreferences

private const val HISTORY_KEY = "history"
class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {
    val sharedPreferences = context.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
    override fun getSearchHistory(): SharedPreferences {
        return sharedPreferences
    }
}