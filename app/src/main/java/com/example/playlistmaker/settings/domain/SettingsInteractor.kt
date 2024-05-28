package com.example.playlistmaker.settings.domain

class SettingsInteractor (private val settingsRepository: SettingsRepository) {
    fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }
    fun updateThemeSetting(isNightModeOn: Boolean) {
        settingsRepository.updateThemeSetting(isNightModeOn)
    }
}