package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackMain = findViewById<Button>(R.id.button_back)
        buttonBackMain.setOnClickListener{
            onBackPressed()
        }

        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switch_dark_theme)
        switchDarkTheme.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val shareApp = findViewById<TextView>(R.id.text_share_app)
        shareApp.setOnClickListener{
            val sendShareAppIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_headerText))
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendShareAppIntent, getString(R.string.share_app_message)))
        }

        val supportText = findViewById<TextView>(R.id.text_support)
        supportText.setOnClickListener{
            val sendSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            }
            startActivity(sendSupportIntent)
        }

        val userAgreementText = findViewById<TextView>(R.id.text_user_agreement)
        userAgreementText.setOnClickListener{
            val sendUserAgreementIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.user_agreement_address))
            }
            startActivity(sendUserAgreementIntent)
        }
    }
}