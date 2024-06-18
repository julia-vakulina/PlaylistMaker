package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository

const val THEME_KEY = "key_switch"
const val THEME_PREFERENCES = "theme_preferences"
class SettingsRepositoryImpl (private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun updateThemeSetting(isNightModeOn: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPreferences.edit().putBoolean(THEME_KEY, isNightModeOn).apply()
    }
}