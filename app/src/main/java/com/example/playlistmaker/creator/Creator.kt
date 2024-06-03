package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.ExternalNavigator
import com.example.playlistmaker.settings.data.NavigatorRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.JsonParserImpl
import com.example.playlistmaker.search.data.TrackGetterRepository
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.JsonParser
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchHistoryRepositoryImpl
import com.example.playlistmaker.settings.domain.NavigatorRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.google.gson.Gson

const val THEME_PREFERENCES = "theme_preferences"
object Creator {
    private lateinit var app: Application
    private var historyRepository: SearchHistoryRepository? = null
    fun initApp(app: App) {
        this.app = app
    }
    private fun getTrackRepository(context: Context ) : TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideTrackInteractor(context: Context ) : TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }
    fun getHistoryRepository() :SearchHistoryRepository{
        if (historyRepository==null ) {
            historyRepository = SearchHistoryRepositoryImpl(this.app.applicationContext)
        }
        return historyRepository as SearchHistoryRepository
    }
    fun provideHistoryInteractor() : HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }
    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractor(provideNavigatorRepository())
    }
    private fun provideNavigatorRepository(): NavigatorRepository {
        return NavigatorRepositoryImpl(
            externalNavigator = provideExternalNavigator(),
            context = app
        )
    }
    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigator(app)
    }
    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractor(provideSettingsRepository())
    }

    private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(
            app.getSharedPreferences(
                THEME_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }
     fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl()
    }

    fun provideJsonParser(): JsonParser {
        return JsonParserImpl(Gson())
    }
}