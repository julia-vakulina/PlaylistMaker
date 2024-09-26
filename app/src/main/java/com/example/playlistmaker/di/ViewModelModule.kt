package com.example.playlistmaker.di

import android.util.Log
import com.example.playlistmaker.media.ui.FavoritesViewModel
import com.example.playlistmaker.media.ui.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.playlistItem.ui.PlaylistItemViewModel
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.ui.EditPlaylistViewModel
import com.example.playlistmaker.playlists.ui.PlaylistViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get(), get())
    }
    viewModel {
        PlayerViewModel(get(), get(), get())
    }
    viewModel {
        SettingsViewModel(androidApplication(), get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
    viewModel { (playlistId: Int) ->
        PlaylistItemViewModel(playlistId, get())
    }
    viewModel { (playlist: Playlist) ->
        EditPlaylistViewModel(get(), playlist)
    }
}