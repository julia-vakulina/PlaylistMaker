package com.example.playlistmaker.settings.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.EmailData
import com.example.playlistmaker.settings.domain.NavigatorRepository

class NavigatorRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context,
) : NavigatorRepository {
    override fun shareApp() {
        externalNavigator.shareLink(context.getString(R.string.share_link_text))
    }

    override fun openTerms() {
        externalNavigator.openLink(context.getString(R.string.legal_link))
    }

    override fun openEmail() {
        externalNavigator.openEmail(
            EmailData(
                title = context.getString(R.string.support_text),
                text = context.getString(R.string.support_subject),
                emailAddress = context.getString(R.string.support_email)
            )
        )
    }
}