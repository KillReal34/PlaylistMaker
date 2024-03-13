package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.dto.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.entities.SettingsUri

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_SWITCH, MODE_PRIVATE)

        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean(SWITCH_KEY, isChecked).apply()
            (applicationContext as App).switchTheme(isChecked)
        }
        binding.switchDarkTheme.isChecked = sharedPref.getBoolean(SWITCH_KEY, false)

        binding.textShareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_headerText))
                type = "text/plain"
                startActivity(Intent.createChooser(this, SettingsUri.SHARE_APP))
            }
        }

        binding.textSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsUri.SUPPORT_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
                startActivity(this)
            }
        }

        binding.textUserAgreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(SettingsUri.USER_AGREEMENT)
                startActivity(this)
            }
        }
    }
}