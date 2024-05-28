package com.example.playlistmaker.search.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.ui.SearchViewModel

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val trackInteractor = Creator.provideTrackInteractor(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(trackInteractor) as T
    }
}