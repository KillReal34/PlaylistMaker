package com.example.playlistmaker.settings.domain.repository

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.entities.Theme

interface ThemeRepository {
    fun set(newTheme: Theme)

    fun getLiveData(): LiveData<Theme>
}