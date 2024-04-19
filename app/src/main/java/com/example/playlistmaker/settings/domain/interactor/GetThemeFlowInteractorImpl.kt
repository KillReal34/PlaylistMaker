package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.entities.Theme
import kotlinx.coroutines.flow.Flow

class GetThemeFlowInteractorImpl(
    private val themeRepository: ThemeRepository,
) : GetThemeFlowInteractor {
    override fun invoke(): Flow<Theme> = themeRepository.getFlow()
}