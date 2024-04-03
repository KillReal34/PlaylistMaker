package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeLiveDataInteractor

class SettingsScreenViewModel(
    private val changeThemeInteractor: ChangeThemeInteractor,
    getThemeLiveDataInteractor: GetThemeLiveDataInteractor,
) : ViewModel() {

    val themeLiveData: LiveData<Theme> = getThemeLiveDataInteractor()

    fun changeTheme(isDark: Boolean) {
        changeThemeInteractor(if (isDark) Theme.DARK else Theme.LIGHT)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dependencyContainer = (this[APPLICATION_KEY] as App).dependencyContainerSettingsScreen
                SettingsScreenViewModel(
                    dependencyContainer.changeThemeInteractor,
                    dependencyContainer.themeLiveDataInteractor,
                )
            }
        }
    }
}