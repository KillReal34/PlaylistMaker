package com.example.playlistmaker.settings.domain.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.entities.Theme

class GetThemeLiveDataInteractorImpl(
    private val themeRepository: ThemeRepository,
) : GetThemeLiveDataInteractor {
    override fun invoke(): LiveData<Theme> = themeRepository.getLiveData()
}