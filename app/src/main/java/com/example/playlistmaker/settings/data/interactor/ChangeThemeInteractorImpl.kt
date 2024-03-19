package com.example.playlistmaker.settings.data.interactor

import com.example.playlistmaker.settings.data.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor

class ChangeThemeInteractorImpl(
    private val themeRepository: ThemeRepository,
) : ChangeThemeInteractor {
    override fun invoke(theme: Theme) = themeRepository.set(newTheme = theme)
}