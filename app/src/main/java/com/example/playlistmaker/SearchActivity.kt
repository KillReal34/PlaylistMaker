package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_EDIT_TEXT = "text"
        const val PLAYLIST_MAKER_PREFERENCES_TRACK = "playlist_maker_preferences"
    }

    private lateinit var searchResultLayout: LinearLayout
    private lateinit var inputSearchText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var textHistoryTitle: TextView
    private lateinit var btnClearHistory: Button

    private lateinit var sharedPref: SharedPreferences

    private val trackAdapter = TrackAdapter()
    private val historyTrackAdapter = TrackAdapter()

    var searchText = ""

    private val baseITunesUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseITunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val trackList = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchResultLayout = findViewById(R.id.searchResultLayout)
        inputSearchText = findViewById(R.id.inputSearchText)
        clearButton = findViewById(R.id.clearIcon)
        backButton = findViewById(R.id.button_back_activitySearch)
        recyclerView = findViewById(R.id.recyclerView)
        errorImage = findViewById(R.id.errorImage)
        errorText = findViewById(R.id.errorText)
        refreshButton = findViewById(R.id.butRefresh)
        textHistoryTitle = findViewById(R.id.textHistoryTitle)
        btnClearHistory = findViewById(R.id.btnClearHistory)

        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_TRACK, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPref)

        trackAdapter.tracks = trackList
        historyTrackAdapter.tracks.addAll(searchHistory.read())

        trackAdapter.onClickListener = {track ->
            val historyTrackIndex = searchHistory.read().indexOf(track)
            if (searchHistory.read().contains(track) && track.trackId == searchHistory.read()[historyTrackIndex].trackId) {
                historyTrackAdapter.tracks.remove(track)
                historyTrackAdapter.tracks.add(0, track)
                searchHistory.write(historyTrackAdapter.tracks)
            }else if (historyTrackAdapter.itemCount < 10) {
                historyTrackAdapter.tracks.add(0, track)
                searchHistory.write(historyTrackAdapter.tracks)
            } else {
                historyTrackAdapter.tracks.add(0, track)
                historyTrackAdapter.tracks.removeAt(10)
                searchHistory.write(historyTrackAdapter.tracks)
            }
        }

        backButton.setOnClickListener{
            onBackPressed()
        }

        clearButton.setOnClickListener {
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            inputSearchText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            recyclerView.visibility = View.VISIBLE
            searchITunesApi(searchText)
        }

        btnClearHistory.setOnClickListener {
            searchHistory.clear()
            historyTrackAdapter.tracks.clear()
            historyTrackAdapter.notifyDataSetChanged()
            textHistoryTitle.visibility = View.GONE
            btnClearHistory.visibility = View.GONE
        }

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchITunesApi(searchText)
                true
            }
            false
        }

        inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && inputSearchText.text.isEmpty() && searchHistory.read()!!.isNotEmpty()) {
                textHistoryTitle.visibility = View.VISIBLE
                btnClearHistory.visibility = View.VISIBLE
                recyclerView.adapter = historyTrackAdapter
                historyTrackAdapter.notifyDataSetChanged()
            } else {
                textHistoryTitle.visibility = View.GONE
                btnClearHistory.visibility = View.GONE
                recyclerView.adapter = trackAdapter
                trackAdapter.notifyDataSetChanged()
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (inputSearchText.hasFocus() && s?.isEmpty() == true && searchHistory.read()!!.isNotEmpty()) {
                    textHistoryTitle.visibility = View.VISIBLE
                    btnClearHistory.visibility = View.VISIBLE
                    recyclerView.adapter = historyTrackAdapter
                    historyTrackAdapter.notifyDataSetChanged()
                } else {
                    textHistoryTitle.visibility = View.GONE
                    btnClearHistory.visibility = View.GONE
                    recyclerView.adapter = trackAdapter
                    trackAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)
        inputSearchText.setText(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putString(KEY_EDIT_TEXT, searchText)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_EDIT_TEXT)

    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchITunesApi(query: String) {
        itunesService.search(query).enqueue(object : Callback<TrackResponse>{
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {

                val trackResponseResult = response.body()?.results

                if (trackResponseResult?.isNotEmpty() == true) {
                    recyclerView.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                    errorText.visibility = View.GONE
                    refreshButton.visibility = View.GONE
                    trackList.clear()
                    trackList.addAll(trackResponseResult)
                    trackAdapter.notifyDataSetChanged()
                }
                else {
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    recyclerView.visibility = View.GONE
                    refreshButton.visibility = View.GONE
                    errorImage.visibility = View.VISIBLE
                    errorText.visibility = View.VISIBLE
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        errorImage.setImageResource(R.drawable.not_found_track_image_night_mode)
                    }
                    else {
                        errorImage.setImageResource(R.drawable.not_found_track_image_light_mode)
                    }
                    errorText.setText(R.string.not_found_track_message)
                }
            }
            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
                recyclerView.visibility = View.GONE
                errorImage.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    errorImage.setImageResource(R.drawable.error_internet_connection_image_night_mode)
                } else {
                    errorImage.setImageResource(R.drawable.error_internet_connection_image_light_mode)
                }
                errorText.setText(R.string.error_internet_connection_message)
                refreshButton.visibility = View.VISIBLE
            }
        })
    }
}
