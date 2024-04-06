package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.entities.Theme
import kotlinx.coroutines.flow.Flow

interface GetThemeFlowInteractor {
    operator fun invoke(): Flow<Theme>
}