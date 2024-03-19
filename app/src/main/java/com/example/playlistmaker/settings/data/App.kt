package com.example.playlistmaker.settings.data

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_SWITCH, MODE_PRIVATE)
        val sharedSwitch = sharedPref.getBoolean(SWITCH_KEY, false)
        switchTheme(sharedSwitch)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}