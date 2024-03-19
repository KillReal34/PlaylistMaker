package com.example.playlistmaker.settings.data.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.data.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.entities.Theme
import com.example.playlistmaker.settings.domain.interactor.GetThemeLiveDataInteractor

class GetThemeLiveDataInteractorImpl(
    private val themeRepository: ThemeRepository,
) : GetThemeLiveDataInteractor {
    override fun invoke(): LiveData<Theme> = themeRepository.getLiveData()
}