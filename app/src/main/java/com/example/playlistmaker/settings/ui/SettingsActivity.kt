package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.App
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.MainActivity
import com.google.android.material.switchmaterial.SwitchMaterial


const val THEME_PREFERENCES = "theme_preferences"
const val THEME_KEY = "key_switch"
class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeSwitcher: SwitchCompat
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.isNightModeOnLiveData.observe(this) {
            switchThemeSwitcher(it)
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        val buttonLeft = findViewById<Button>(R.id.button_left)
        val buttonShare = findViewById<Button>(R.id.button_share)
        val buttonSupport = findViewById<Button>(R.id.button_support)
        val buttonLegal = findViewById<Button>(R.id.button_legal)

        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }
        buttonShare.setOnClickListener {
            viewModel.shareApp()
        }
        buttonSupport.setOnClickListener {
            viewModel.openEmail()
        }
        buttonLegal.setOnClickListener {
            viewModel.openTerms()
        }

    }
    private fun switchThemeSwitcher(isNightModeOn: Boolean) {
        themeSwitcher.isChecked = isNightModeOn
    }
}

