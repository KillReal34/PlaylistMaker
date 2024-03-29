package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val baseITunesUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseITunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)
    override fun doRequest(dto: Any) : Response {
        if (dto is TrackSearchRequest) {

            val resp = itunesService.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { Response().resultCode = resp.code() } as Response
        }
        else {
            return Response().apply { resultCode = 400 }
        }
    }
}