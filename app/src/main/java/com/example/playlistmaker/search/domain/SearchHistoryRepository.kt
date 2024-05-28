package com.example.playlistmaker.search.domain

import android.content.SharedPreferences

interface SearchHistoryRepository {
    fun getSearchHistory() : SharedPreferences
}