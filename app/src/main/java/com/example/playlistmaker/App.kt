package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.ui.THEME_KEY
import com.example.playlistmaker.settings.ui.THEME_PREFERENCES


class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.initApp(this)
        val settingsInteractor = Creator.provideSettingsInteractor()
        darkTheme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(darkTheme)
    }
}
