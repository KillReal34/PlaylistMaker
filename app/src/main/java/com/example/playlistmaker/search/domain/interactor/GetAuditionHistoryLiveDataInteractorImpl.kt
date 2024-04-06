package com.example.playlistmaker.search.domain.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.entities.AuditionHistory

class GetAuditionHistoryLiveDataInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository
): GetAuditionHistoryLiveDataInteractor {
    override fun invoke(): LiveData<AuditionHistory> = auditionHistoryRepository.getLiveData()
}