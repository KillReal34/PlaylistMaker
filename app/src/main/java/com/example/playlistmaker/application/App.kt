package com.example.playlistmaker.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.settings.domain.entities.Theme

class App : Application() {

    lateinit var dependencyContainerSearchScreen: DependencyContainer
        private set
    lateinit var dependencyContainerSettingsScreen: DependencyContainer
        private set
    private lateinit var themeLiveData: LiveData<Theme>

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_TRACK = "playlist_maker_preferences"
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
        const val AUDITION_HISTORY_KEY = "auditionHistoryKey"
    }

    override fun onCreate() {
        super.onCreate()
        val sharedPrefSettingScreen =
            getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_SWITCH, MODE_PRIVATE)
        val sharedPrefSearchScreen =
            getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_TRACK, MODE_PRIVATE)

        dependencyContainerSearchScreen =
            DependencyContainer(AUDITION_HISTORY_KEY, sharedPrefSearchScreen)

        dependencyContainerSettingsScreen = DependencyContainer(SWITCH_KEY, sharedPrefSettingScreen)
        themeLiveData = dependencyContainerSettingsScreen.themeLiveDataInteractor().asLiveData()
        themeLiveData.observeForever { theme ->
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