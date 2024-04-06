package com.example.playlistmaker.settings.data.repository

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.data.persistence.ThemePersistence
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.repository.ThemeRepository

class ThemeRepositoryImpl(
    private val themePersistence: ThemePersistence,
) : ThemeRepository {
    override fun set(newTheme: Theme) = themePersistence.set(newTheme = newTheme)

    override fun getLiveData(): LiveData<Theme> = themePersistence.getLiveData()
}