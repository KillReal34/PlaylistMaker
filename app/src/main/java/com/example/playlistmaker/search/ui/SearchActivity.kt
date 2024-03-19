package com.example.playlistmaker.search.ui

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.SearchHistory
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.data.TrackResponse
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.presentation.TrackAdapter
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

    private lateinit var binding: ActivitySearchBinding

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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.buttonBackActivitySearch.setOnClickListener{
            onBackPressed()
        }

        binding.ivClearIcon.setOnClickListener {
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            binding.etInputSearchText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.btnRefresh.setOnClickListener {
            binding.recyclerView.isVisible = true
            searchITunesApi(searchText)
        }

        binding.btnClearHistory.setOnClickListener {
            searchHistory.clear()
            historyTrackAdapter.tracks.clear()
            historyTrackAdapter.notifyDataSetChanged()
            binding.tvTextHistoryTitle.isVisible = false
            binding.btnClearHistory.isVisible = false
        }

        binding.etInputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchITunesApi(searchText)
                true
            }
            false
        }

        binding.etInputSearchText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && binding.etInputSearchText.text!!.isEmpty()  && searchHistory.read().isNotEmpty()) {
                binding.tvTextHistoryTitle.isVisible = true
                binding.btnClearHistory.isVisible = true
                binding.recyclerView.adapter = historyTrackAdapter
                historyTrackAdapter.notifyDataSetChanged()
            } else {
                binding.tvTextHistoryTitle.isVisible = false
                binding.btnClearHistory.isVisible = false
                binding.recyclerView.adapter = trackAdapter
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

                binding.ivClearIcon.visibility = clearButtonVisibility(s)
                if (binding.etInputSearchText.hasFocus() && s?.isEmpty() == true && searchHistory.read().isNotEmpty()) {
                    binding.tvTextHistoryTitle.isVisible = true
                    binding.btnClearHistory.isVisible = true
                    binding.recyclerView.adapter = historyTrackAdapter
                    historyTrackAdapter.notifyDataSetChanged()
                } else {
                    binding.tvTextHistoryTitle.isVisible = false
                    binding.btnClearHistory.isVisible = false
                    binding.recyclerView.adapter = trackAdapter
                    trackAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        binding.etInputSearchText.addTextChangedListener(simpleTextWatcher)
        binding.etInputSearchText.setText(searchText)
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
        if (binding.etInputSearchText.text!!.isNotEmpty()) {
            binding.progressBar.isVisible = true
            binding.scrollView.isVisible = false

            itunesService.search(query).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                    binding.progressBar.isVisible = false
                    binding.scrollView.isVisible = true

                    val trackResponseResult = response.body()?.results

                    if (trackResponseResult?.isNotEmpty() == true) {
                        binding.recyclerView.isVisible = true
                        binding.tvErrorText.isVisible = false
                        binding.ivErrorImage.isVisible = false
                        binding.btnRefresh.isVisible = false
                        trackList.clear()
                        trackList.addAll(trackResponseResult)
                        trackAdapter.notifyDataSetChanged()
                    }
                    else {
                        trackList.clear()
                        trackAdapter.notifyDataSetChanged()
                        binding.recyclerView.isVisible = false
                        binding.btnRefresh.isVisible = false
                        binding.ivErrorImage.isVisible = true
                        binding.tvErrorText.isVisible = true
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                            binding.ivErrorImage.setImageResource(R.drawable.not_found_track_image_night_mode)
                        }
                        else {
                            binding.ivErrorImage.setImageResource(R.drawable.not_found_track_image_light_mode)
                        }
                        binding.tvErrorText.setText(R.string.not_found_track_message)
                    }
                }
                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    binding.scrollView.isVisible = true
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    binding.recyclerView.isVisible = false
                    binding.ivErrorImage.isVisible = true
                    binding.tvErrorText.isVisible = true
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        binding.ivErrorImage.setImageResource(R.drawable.error_internet_connection_image_night_mode)
                    } else {
                        binding.ivErrorImage.setImageResource(R.drawable.error_internet_connection_image_light_mode)
                    }
                    binding.tvErrorText.setText(R.string.error_internet_connection_message)
                    binding.btnRefresh.isVisible = true
                }
            })
        }
    }
}
