package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.entities.Theme

class ChangeThemeInteractorImpl(
    private val themeRepository: ThemeRepository,
) : ChangeThemeInteractor {
    override fun invoke(theme: Theme) = themeRepository.set(newTheme = theme)
}