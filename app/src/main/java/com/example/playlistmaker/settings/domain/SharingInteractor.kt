package com.example.playlistmaker.settings.domain

class SharingInteractor(private val navigatorRepository: NavigatorRepository) {

    fun shareApp() {
        navigatorRepository.shareApp()
    }

    fun openTerms() {
        navigatorRepository.openTerms()
    }

    fun openEmail() {
        navigatorRepository.openEmail()
    }
}