package com.example.playlistmaker

import android.content.SharedPreferences
import android.view.ViewGroup
import com.google.gson.Gson

const val USER_KEY = "user_key"
class SearchHistory(val sharedPref: SharedPreferences) {

    fun read() : Array<Track> {
        val json = sharedPref.getString(USER_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun write(tracks: ArrayList<Track>){
        val json = Gson().toJson(tracks)
        sharedPref.edit().putString(USER_KEY, json).apply()
    }

    fun clear() {
        sharedPref.edit().clear().apply()
    }
}