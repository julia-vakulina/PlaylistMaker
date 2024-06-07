package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : AndroidViewModel(application) {

    //companion object {

     //   fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
     //       initializer {

      //          SettingsViewModel(
      //              application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application,
      //              sharingInteractor = Creator.provideSharingInteractor(),
      //              settingsInteractor =  Creator.provideSettingsInteractor()
      //          )
      //      }
      //  }
    //}

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