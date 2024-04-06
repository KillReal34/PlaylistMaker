package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository

class ClearSearchHistoryInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository
): ClearSearchHistoryInteractor {
    override fun invoke() = auditionHistoryRepository.clear()
}