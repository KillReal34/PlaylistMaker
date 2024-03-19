package com.example.playlistmaker.search.domain.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.search.domain.entities.AuditionHistory

interface GetAuditionHistoryLiveDataInteractor {
    operator fun invoke(): LiveData<AuditionHistory>
}