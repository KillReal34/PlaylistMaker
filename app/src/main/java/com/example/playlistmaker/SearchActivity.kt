package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_EDIT_TEXT = "text"
        const val PLAYLIST_MAKER_PREFERENCES_TRACK = "playlist_maker_preferences"
        const val TRACK_EXTRA = "track_extra"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchResultLayout: LinearLayout? = null
    private var inputSearchText: EditText? = null
    private var clearButton: ImageView? = null
    private var backButton: Button? = null
    private var recyclerView: RecyclerView? = null
    private var errorImage: ImageView? = null
    private var errorText: TextView? = null
    private var refreshButton: Button? = null
    private var textHistoryTitle: TextView? = null
    private var btnClearHistory: Button? = null
    private var progressBar: ProgressBar? = null
    private var scrollView: ScrollView? = null

    private val trackAdapter = TrackAdapter()
    private val historyTrackAdapter = TrackAdapter()

    var searchText = ""

    private val baseITunesUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseITunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val handlerSearch = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchITunesApi(searchText) }

    private val trackList = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchResultLayout = findViewById(R.id.ll_search_result_layout)
        inputSearchText = findViewById(R.id.et_input_search_text)
        clearButton = findViewById(R.id.iv_clear_icon)
        backButton = findViewById(R.id.button_back_activitySearch)
        recyclerView = findViewById(R.id.recycler_view)
        errorImage = findViewById(R.id.iv_error_image)
        errorText = findViewById(R.id.tv_error_text)
        refreshButton = findViewById(R.id.btn_refresh)
        textHistoryTitle = findViewById(R.id.tv_text_history_title)
        btnClearHistory = findViewById(R.id.btn_clear_history)
        progressBar = findViewById(R.id.progressBar)
        scrollView = findViewById(R.id.scrollView)

        val sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES_TRACK, MODE_PRIVATE)
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

            if (trackAdapter.clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                audioPlayerIntent.putExtra(TRACK_EXTRA, track)
                startActivity(audioPlayerIntent)
            }
        }

        historyTrackAdapter.onClickListener = {track ->
            if (historyTrackAdapter.clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                audioPlayerIntent.putExtra(TRACK_EXTRA, track)
                startActivity(audioPlayerIntent)
            }
        }

        backButton?.setOnClickListener{
            onBackPressed()
        }

        clearButton?.setOnClickListener {
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            inputSearchText?.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        refreshButton?.setOnClickListener {
            recyclerView?.isVisible = true
            searchITunesApi(searchText)
        }

        btnClearHistory?.setOnClickListener {
            searchHistory.clear()
            historyTrackAdapter.tracks.clear()
            historyTrackAdapter.notifyDataSetChanged()
            textHistoryTitle?.isVisible = false
            btnClearHistory?.isVisible = false
        }

        inputSearchText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchITunesApi(searchText)
                true
            }
            false
        }

        inputSearchText?.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && inputSearchText?.text!!.isEmpty()  && searchHistory.read().isNotEmpty()) {
                textHistoryTitle?.isVisible = true
                btnClearHistory?.isVisible = true
                recyclerView?.adapter = historyTrackAdapter
                historyTrackAdapter.notifyDataSetChanged()
            } else {
                textHistoryTitle?.isVisible = false
                btnClearHistory?.isVisible = false
                recyclerView?.adapter = trackAdapter
                trackAdapter.notifyDataSetChanged()
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()

                searchDebounce()

                clearButton?.visibility = clearButtonVisibility(s)
                if (inputSearchText?.hasFocus() == true && s?.isEmpty() == true && searchHistory.read().isNotEmpty()) {
                    textHistoryTitle?.isVisible = true
                    btnClearHistory?.isVisible = true
                    recyclerView?.adapter = historyTrackAdapter
                    historyTrackAdapter.notifyDataSetChanged()
                } else {
                    textHistoryTitle?.isVisible = false
                    btnClearHistory?.isVisible = false
                    recyclerView?.adapter = trackAdapter
                    trackAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        inputSearchText?.addTextChangedListener(simpleTextWatcher)
        inputSearchText?.setText(searchText)
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

    private fun searchDebounce() {
        handlerSearch.removeCallbacks(searchRunnable)
        handlerSearch.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchITunesApi(query: String) {
        if (inputSearchText?.text!!.isNotEmpty()) {
            progressBar?.isVisible = true
            scrollView?.isVisible = false

            itunesService.search(query).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                    progressBar?.isVisible = false
                    scrollView?.isVisible = true

                    val trackResponseResult = response.body()?.results

                    if (trackResponseResult?.isNotEmpty() == true) {
                        recyclerView?.isVisible = true
                        errorText?.isVisible = false
                        errorImage?.isVisible = false
                        refreshButton?.isVisible = false
                        trackList.clear()
                        trackList.addAll(trackResponseResult)
                        trackAdapter.notifyDataSetChanged()
                    }
                    else {
                        trackList.clear()
                        trackAdapter.notifyDataSetChanged()
                        recyclerView?.isVisible = false
                        refreshButton?.isVisible = false
                        errorImage?.isVisible = true
                        errorText?.isVisible = true
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                            errorImage?.setImageResource(R.drawable.not_found_track_image_night_mode)
                        }
                        else {
                            errorImage?.setImageResource(R.drawable.not_found_track_image_light_mode)
                        }
                        errorText?.setText(R.string.not_found_track_message)
                    }
                }
                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progressBar?.isVisible = false
                    scrollView?.isVisible = true

                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    recyclerView?.isVisible = false
                    errorImage?.isVisible = true
                    errorText?.isVisible = true
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        errorImage?.setImageResource(R.drawable.error_internet_connection_image_night_mode)
                    } else {
                        errorImage?.setImageResource(R.drawable.error_internet_connection_image_light_mode)
                    }
                    errorText?.setText(R.string.error_internet_connection_message)
                    refreshButton?.isVisible = true
                }
            })
        }
    }
}
