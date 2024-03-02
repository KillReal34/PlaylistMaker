package com.example.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.data.dto.App
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
    }

    private var buttonBackMain: Button? = null
    private var switchDarkTheme: SwitchCompat? = null
    private var shareApp: TextView? = null
    private var supportText: TextView? = null
    private var userAgreementText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonBackMain = findViewById(R.id.button_back)
        buttonBackMain!!.setOnClickListener{
            onBackPressed()
        }

        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_SWITCH, MODE_PRIVATE)

        switchDarkTheme = findViewById(R.id.switch_dark_theme)
        switchDarkTheme!!.setOnCheckedChangeListener{buttonView, isChecked ->
            sharedPref.edit().putBoolean(SWITCH_KEY, switchDarkTheme!!.isChecked).apply()
            (applicationContext as App).switchTheme(isChecked)
        }
        switchDarkTheme!!.isChecked = sharedPref.getBoolean(SWITCH_KEY, false)

        shareApp = findViewById(R.id.text_share_app)
        shareApp!!.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_headerText))
                type = "text/plain"
                startActivity(Intent.createChooser(this, getString(R.string.share_app_message)))
            }
        }

        supportText = findViewById(R.id.text_support)
        supportText!!.setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
                startActivity(this)
            }
        }

        userAgreementText = findViewById(R.id.text_user_agreement)
        userAgreementText!!.setOnClickListener{
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.user_agreement_address))
                startActivity(this)
            }
        }
    }
}