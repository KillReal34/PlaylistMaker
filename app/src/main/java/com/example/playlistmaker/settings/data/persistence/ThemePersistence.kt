package com.example.playlistmaker.settings.data.persistence

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.entities.Theme

interface ThemePersistence {
    fun set(newTheme: Theme)

    fun getLiveData(): LiveData<Theme>
}