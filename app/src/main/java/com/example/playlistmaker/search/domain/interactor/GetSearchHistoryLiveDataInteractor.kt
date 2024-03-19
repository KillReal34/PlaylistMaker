package com.example.playlistmaker.search.domain.interactor

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.entities.Track

interface GetSearchHistoryLiveDataInteractor {
    operator fun invoke(): LiveData<List<Track>>
}