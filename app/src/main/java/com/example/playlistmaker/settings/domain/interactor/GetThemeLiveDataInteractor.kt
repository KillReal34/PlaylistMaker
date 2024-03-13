package com.example.playlistmaker.settings.domain.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.entities.Theme

interface GetThemeLiveDataInteractor {
    operator fun invoke() : LiveData<Theme>
}