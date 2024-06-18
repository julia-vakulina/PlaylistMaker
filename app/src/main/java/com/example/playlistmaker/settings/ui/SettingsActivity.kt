package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var themeSwitcher: SwitchCompat
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


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

