package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.data.network.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.time.Duration.Companion.milliseconds

class TrackRepositoryImpl(private val iTunesApi: ITunesApi) : TrackRepository {
    override fun searchByName(
        pattern: String,
        onSuccess: (trackList: List<Track>) -> Unit,
        onFailure: (exception: Throwable) -> Unit,
    ) {
        val callback = object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                val responseTrackList = response.body()?.results
                val domainTrackList = if (responseTrackList.isNullOrEmpty()) {
                    emptyList()
                } else {
                    responseTrackList.map { trackDto ->
                        Track(
                            trackId = trackDto.trackId.toString(),
                            collectionName = trackDto.collectionName,
                            releaseDate = trackDto.releaseDate,
                            primaryGenreName = trackDto.primaryGenreName,
                            country = trackDto.country,
                            trackName = trackDto.trackName,
                            artistName = trackDto.artistName,
                            duration = trackDto.trackTimeMillis.milliseconds,
                            artworkUrl = trackDto.artworkUrl100,
                            previewUrl = trackDto.previewUrl,
                        )
                    }
                }

                onSuccess(domainTrackList)
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) = onFailure(t)
        }

        iTunesApi.search(text = pattern).enqueue(callback)
    }
}
