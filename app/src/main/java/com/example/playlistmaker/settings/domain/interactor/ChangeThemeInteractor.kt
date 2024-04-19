package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.entities.Theme

interface ChangeThemeInteractor {
    operator fun invoke(theme: Theme)
}