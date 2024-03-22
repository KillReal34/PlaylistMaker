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
) : AuditionHistoryPersistence {

    private val gson = Gson()
    override fun add(track: Track) {
        val json = sharedPreferences.getString(auditionHistoryKey, null)
        val historyTrackList = gson.fromJson(json, Array<Track>::class.java).toMutableList()

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

    override fun clear() = sharedPreferences.edit { clear() }

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
                    if (auditionHistoryKey != key) return

                    postValue(sharedPreferences?.getAuditionHistoryValue())
                }
            }
        }

    private fun SharedPreferences.getAuditionHistoryValue(): AuditionHistory {
        val trackListJson = getString(auditionHistoryKey, null)
        val trackList = if (trackListJson.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(trackListJson, Array<Track>::class.java).toList()
        }

        return AuditionHistory(trackList)
    }
}
