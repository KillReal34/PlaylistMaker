package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractor

class SettingsScreenViewModel(
    private val changeThemeInteractor: ChangeThemeInteractor,
    getThemeFlowInteractor: GetThemeFlowInteractor,
) : ViewModel() {

    val themeLiveData: LiveData<Theme> = getThemeFlowInteractor().asLiveData()

    fun changeTheme(isDark: Boolean) {
        changeThemeInteractor(if (isDark) Theme.DARK else Theme.LIGHT)
    }
}