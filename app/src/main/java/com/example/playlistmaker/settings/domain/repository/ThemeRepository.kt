package com.example.playlistmaker.settings.domain.repository

import com.example.playlistmaker.settings.domain.entities.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun set(newTheme: Theme)

    fun getFlow(): Flow<Theme>
}