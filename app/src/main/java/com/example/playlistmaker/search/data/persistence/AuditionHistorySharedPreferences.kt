package com.example.playlistmaker.search.data.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import com.google.gson.Gson

class AuditionHistorySharedPreferences(
    private val auditionHistoryKey: String,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : AuditionHistoryPersistence {

    override fun add(track: Track) {
        val json = sharedPreferences.getString(auditionHistoryKey, null)
        val historyTrackList = if (json != null) {
            gson.fromJson(json, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        val historyTrackListIndex = historyTrackList.indexOfFirst { it.trackId == track.trackId }
        val maxHistorySize = 10

        historyTrackList.add(0, track)
        if (historyTrackListIndex >= 0) {
            historyTrackList.removeAt(historyTrackListIndex)
        } else if (historyTrackList.size >= maxHistorySize) {
            historyTrackList.removeLastOrNull()
        }

        sharedPreferences.edit {
            putString(auditionHistoryKey, gson.toJson(historyTrackList))
        }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    override fun getLiveData(): LiveData<AuditionHistory> =
        object : LiveData<AuditionHistory>(sharedPreferences.getAuditionHistoryValue()) {
            private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

            override fun onActive() {
                super.onActive()

                if (listener == null) {
                    listener = provideListener(auditionHistoryKey)
                }

                sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            }

            override fun onInactive() {
                super.onInactive()

                if (hasActiveObservers()) return

                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener ?: return)
            }

            private fun provideListener(
                auditionHistoryKey: String,
            ) = object : SharedPreferences.OnSharedPreferenceChangeListener {
                override fun onSharedPreferenceChanged(
                    sharedPreferences: SharedPreferences?,
                    key: String?
                ) {
                    if (key != null && auditionHistoryKey != key) return

                    postValue(sharedPreferences?.getAuditionHistoryValue(key = key))
                }
            }
        }

    private fun SharedPreferences.getAuditionHistoryValue(
        key: String? = auditionHistoryKey,
    ): AuditionHistory {
        if (key == null) return AuditionHistory(trackList = emptyList())

        val trackListJson = getString(key, null)
        val trackList = if (trackListJson.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(trackListJson, Array<Track>::class.java).toList()
        }

        return AuditionHistory(trackList = trackList)
    }
}
