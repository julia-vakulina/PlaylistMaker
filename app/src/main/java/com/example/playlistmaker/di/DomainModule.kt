package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.creator.THEME_PREFERENCES
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.data.JsonParserImpl
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.JsonParser
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.settings.data.ExternalNavigator
import com.example.playlistmaker.settings.data.NavigatorRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.NavigatorRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractor(get())
    }
    single<NavigatorRepository> {
        NavigatorRepositoryImpl(get(), androidContext())
    }
    single<ExternalNavigator> {
        ExternalNavigator(androidContext())
    }
    single<SettingsInteractor> {
        SettingsInteractor(get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext().getSharedPreferences(
            THEME_PREFERENCES,
            Context.MODE_PRIVATE))
    }
    single<JsonParser>{
        JsonParserImpl(get())
    }
    single { MediaPlayer() }
}