package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : AndroidViewModel(application) {


    private var isNightModeOnMutableLiveData = MutableLiveData<Boolean>()
    val isNightModeOnLiveData: LiveData<Boolean> = isNightModeOnMutableLiveData

    init {
        getIsNightModeOn()
    }

    fun switchTheme(isNightModeOn: Boolean) {
        settingsInteractor.updateThemeSetting(isNightModeOn)
        isNightModeOnMutableLiveData.value = isNightModeOn
    }

    private fun getIsNightModeOn() {
        isNightModeOnMutableLiveData.value = settingsInteractor.getThemeSettings()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openEmail() {
        sharingInteractor.openEmail()
    }
}