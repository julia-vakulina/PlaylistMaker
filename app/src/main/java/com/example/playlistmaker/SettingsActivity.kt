package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


const val THEME_PREFERENCES = "theme_preferences"
const val THEME_KEY = "key_switch"
class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        themeSwitcher.isChecked = sharedPrefs.getBoolean(THEME_KEY, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(THEME_KEY, checked).apply()
        }

        val buttonLeft = findViewById<Button>(R.id.button_left)
        val buttonShare = findViewById<Button>(R.id.button_share)
        val buttonSupport = findViewById<Button>(R.id.button_support)
        val buttonLegal = findViewById<Button>(R.id.button_legal)

        buttonLeft.setOnClickListener {
            val leftIntent = Intent(this, MainActivity::class.java)
            startActivity(leftIntent)
        }
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareLink = getString(R.string.share_link_text)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }
        buttonSupport.setOnClickListener {
            val message = getString(R.string.support_text)
            val email = getString(R.string.support_email)
            val subject = getString(R.string.support_subject)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.type = "text/plain"
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, email)
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }
        buttonLegal.setOnClickListener {
            val legalIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.legal_link)))
            startActivity(legalIntent)
        }
    }
}

