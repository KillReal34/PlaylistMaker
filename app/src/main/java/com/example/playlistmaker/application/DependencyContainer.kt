package com.example.playlistmaker.application

import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.interactor.ChangeThemeInteractorImpl
import com.example.playlistmaker.settings.data.interactor.GetThemeLiveDataInteractorImpl
import com.example.playlistmaker.settings.data.persistence.ThemePersistence
import com.example.playlistmaker.settings.data.persistence.ThemeSharedPreferences
import com.example.playlistmaker.settings.data.repository.ThemeRepository
import com.example.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeLiveDataInteractor

class DependencyContainer(
    themeKey: String,
    themeSharedPreferences: SharedPreferences
) {
    private val themePersistence: ThemePersistence = ThemeSharedPreferences(
        themeKey = themeKey,
        sharedPreferences = themeSharedPreferences,
    )

    private val themeRepository: ThemeRepository =
        ThemeRepositoryImpl(themePersistence = themePersistence)

    val changeThemeInteractor: ChangeThemeInteractor =
        ChangeThemeInteractorImpl(themeRepository = themeRepository)

    val themeLiveDataInteractor: GetThemeLiveDataInteractor =
        GetThemeLiveDataInteractorImpl(themeRepository = themeRepository)
}