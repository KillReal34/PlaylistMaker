package com.example.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractor

class MainActivityViewModel(
    getThemeFlowInteractor: GetThemeFlowInteractor,
) : ViewModel() {
    val isNightThemeFlow = getThemeFlowInteractor().asLiveData().map(Theme::isDark)
}