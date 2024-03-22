package com.example.playlistmaker.search.data.interactor

import com.example.playlistmaker.search.data.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor

class ClearSearchHistoryInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository
): ClearSearchHistoryInteractor {
    override fun invoke() = auditionHistoryRepository.clear()
}