package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.ui.THEME_KEY
import com.example.playlistmaker.settings.ui.THEME_PREFERENCES
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }
        //Creator.initApp(this)
        val settingsInteractor: SettingsInteractor by inject()
        darkTheme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(darkTheme)
    }
}
