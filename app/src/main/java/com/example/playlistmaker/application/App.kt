package com.example.playlistmaker.application

import android.app.Application
import com.example.playlistmaker.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES_TRACK = "playlist_maker_preferences"
        const val PLAYLIST_MAKER_PREFERENCES_SWITCH = "playlist_maker_preferences_switch"
        const val SWITCH_KEY = "switchKey"
        const val AUDITION_HISTORY_KEY = "auditionHistoryKey"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(applicationModule)
        }
    }
}
