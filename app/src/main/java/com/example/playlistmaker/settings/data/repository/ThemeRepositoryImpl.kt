package com.example.playlistmaker.settings.data.repository

import androidx.lifecycle.asFlow
import com.example.playlistmaker.settings.data.persistence.ThemePersistence
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(
    private val themePersistence: ThemePersistence,
) : ThemeRepository {
    override fun set(newTheme: Theme) = themePersistence.set(newTheme = newTheme)

    override fun getFlow(): Flow<Theme> = themePersistence.getLiveData().asFlow()
}