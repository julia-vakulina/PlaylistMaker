package com.example.playlistmaker.search.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val trackInteractor = Creator.provideTrackInteractor(context)
    private val historyInteractor = Creator.provideHistoryInteractor()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(trackInteractor, historyInteractor) as T
    }
}