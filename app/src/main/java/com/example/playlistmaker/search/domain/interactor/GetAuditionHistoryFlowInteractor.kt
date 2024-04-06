package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.entities.AuditionHistory
import kotlinx.coroutines.flow.Flow

interface GetAuditionHistoryFlowInteractor {
    operator fun invoke(): Flow<AuditionHistory>
}