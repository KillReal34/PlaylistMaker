package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import kotlinx.coroutines.flow.Flow

class GetAuditionHistoryFlowInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository
): GetAuditionHistoryFlowInteractor {
    override fun invoke(): Flow<AuditionHistory> = auditionHistoryRepository.getFlow()
}