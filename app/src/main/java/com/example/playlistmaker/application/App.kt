package com.example.playlistmaker.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.entities.Theme

class App : Application() {

    lateinit var dependencyContainer: DependencyContainer
    private lateinit var themeLiveData: LiveData<Theme>

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
    }

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_SWITCH, MODE_PRIVATE)


        dependencyContainer = DependencyContainer(SWITCH_KEY, sharedPref)
        themeLiveData = dependencyContainer.themeLiveDataInteractor()
        themeLiveData.observeForever {theme ->
            AppCompatDelegate.setDefaultNightMode(
                when (theme) {
                    Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    null -> return@observeForever
                }
            )
        }
    }
}