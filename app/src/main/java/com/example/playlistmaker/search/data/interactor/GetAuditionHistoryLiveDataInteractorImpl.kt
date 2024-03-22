package com.example.playlistmaker.search.data.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.search.data.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryLiveDataInteractor

class GetAuditionHistoryLiveDataInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository
): GetAuditionHistoryLiveDataInteractor {
    override fun invoke(): LiveData<AuditionHistory> = auditionHistoryRepository.getLiveData()
}