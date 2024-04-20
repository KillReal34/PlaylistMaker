package com.example.playlistmaker.di

import android.app.Application
import android.content.Context
import com.example.playlistmaker.application.App
import com.example.playlistmaker.search.data.persistence.AuditionHistoryPersistence
import com.example.playlistmaker.search.data.persistence.AuditionHistorySharedPreferences
import com.example.playlistmaker.settings.data.persistence.ThemePersistence
import com.example.playlistmaker.settings.data.persistence.ThemeSharedPreferences
import com.google.gson.Gson
import org.koin.dsl.bind
import org.koin.dsl.module

val persistenceModule = module {
    single {
        val sharedPreferences = get<Context>()
            .getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES_SWITCH, Application.MODE_PRIVATE)

        ThemeSharedPreferences(
            themeKey = App.SWITCH_KEY,
            sharedPreferences = sharedPreferences,
        )
    } bind ThemePersistence::class

    single {
        val sharedPreferences = get<Context>()
            .getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES_TRACK, Application.MODE_PRIVATE)

        AuditionHistorySharedPreferences(
            auditionHistoryKey = App.AUDITION_HISTORY_KEY,
            sharedPreferences = sharedPreferences,
            gson = Gson(),
        )
    } bind AuditionHistoryPersistence::class
}
