package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.player.ui.PlayerTrack
import kotlin.time.Duration.Companion.seconds

class SearchActivity : AppCompatActivity() {

    companion object {
        private val SEARCH_DEBOUNCE_DELAY = 2.seconds
    }

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchScreenViewModel by viewModels(factoryProducer = SearchScreenViewModel::Factory)

    private val searchTrackListAdapter = TrackAdapter(onTrackClick = ::onSearchedTackClick)

    private val historyTrackAdapter = TrackAdapter(onTrackClick = ::onHistoryTrackClick)

    private val handlerSearch = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable {
        viewModel.searchTrackByName(namePattern = inputSearchText ?: return@Runnable)
    }

    private var inputSearchText: String?
        get() = withBinding { etInputSearchText.text?.toString() }
        set(value) = withBinding { etInputSearchText.setText(value) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        withBinding {
            setContentView(root)

            buttonBackActivitySearch.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            ivClearIcon.setOnClickListener { view ->
                inputSearchText = ""
                viewModel.setAuditionHistoryTrack()
                (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            btnRefresh.setOnClickListener { searchDebounce() }

            btnClearHistory.setOnClickListener { viewModel.clearAuditionHistory() }

            with(etInputSearchText) {
                setOnEditorActionListener { _, actionId, _ ->
                    val namePattern = inputSearchText

                    if (actionId == EditorInfo.IME_ACTION_DONE && namePattern?.isNotEmpty() == true) {
                        stopDebounceSearch()
                        viewModel.searchTrackByName(namePattern = namePattern)
                    }

                    return@setOnEditorActionListener false
                }

                setOnFocusChangeListener { inputView, hasFocus ->
                    val isTextFieldNotEmpty = (inputView as? EditText)?.text?.isNotEmpty() == true

                    setClearButtonAvailability(
                        searchFieldIsNotEmpty = isTextFieldNotEmpty,
                        searchFieldHasFocus = hasFocus,
                    )

                    if (isTextFieldNotEmpty) return@setOnFocusChangeListener

                    if (hasFocus) viewModel.setAuditionHistoryTrack() else viewModel.setIdleState()
                }

                doAfterTextChanged(action = ::onSearchTextChanged)
            }
        }

        viewModel.screenStateLiveData.observe(this) { state ->
            when (state) {
                is SearchScreenState.AuditionHistory ->
                    drawAuditionHistoryState(historyTrackList = state.historyTrackList)

                is SearchScreenState.Idle -> drawIdleState()

                is SearchScreenState.IsLoading -> drawLoadingState()

                is SearchScreenState.OnSearchError -> drawErrorState()

                is SearchScreenState.SearchedTrackResult ->
                    drawSearchResultState(searchedTrackList = state.searchedTrackList)
            }
        }
    }

    private fun stopDebounceSearch() = handlerSearch.removeCallbacks(searchRunnable)

    private fun searchDebounce() {
        stopDebounceSearch()
        handlerSearch.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY.inWholeMilliseconds)
    }

    private fun onSearchTextChanged(newText: CharSequence?) {
        val searchIsNotEmpty = newText?.isNotEmpty() == true

        if (searchIsNotEmpty) searchDebounce()

        setClearButtonAvailability(searchFieldIsNotEmpty = searchIsNotEmpty)
    }

    private fun setClearButtonAvailability(
        searchFieldHasFocus: Boolean = withBinding { etInputSearchText.hasFocus() },
        searchFieldIsNotEmpty: Boolean = inputSearchText?.isNotEmpty() == true,
    ): Unit = withBinding {
        val isAvailable = searchFieldHasFocus && searchFieldIsNotEmpty

        with(ivClearIcon) {
            isVisible = isAvailable
            isClickable = isAvailable
        }
    }

    private fun createAudioPlayerIntent(track: Track) = Intent(
        this,
        AudioPlayerActivity::class.java,
    ).apply {
        putExtra(AudioPlayerActivity.TRACK_EXTRA, PlayerTrack(track = track))
    }

    private fun onSearchedTackClick(track: Track) {
        viewModel.addTrackToAuditionHistory(track = track)

        startActivity(createAudioPlayerIntent(track = track))
    }

    private fun onHistoryTrackClick(track: Track) =
        startActivity(createAudioPlayerIntent(track = track))

    private fun drawIdleState(): Unit = withBinding {
        progressBar.isGone = true
        scrollView.isGone = true
    }

    private fun drawAuditionHistoryState(historyTrackList: List<Track>): Unit = withBinding {
        progressBar.isGone = true
        scrollView.isVisible = true

        val isHistoryEmpty = historyTrackList.isEmpty()

        tvTextHistoryTitle.isGone = isHistoryEmpty

        with(recyclerView) {
            isGone = isHistoryEmpty

            if (isHistoryEmpty) return@with

            historyTrackAdapter.trackList = historyTrackList

            if (adapter != historyTrackAdapter) {
                adapter = historyTrackAdapter
            }
        }

        btnClearHistory.isGone = isHistoryEmpty
        ivErrorImage.isGone = true
        tvErrorText.isGone = true
        btnRefresh.isGone = true
    }

    private fun drawLoadingState(): Unit = withBinding {
        progressBar.isVisible = true
        scrollView.isGone = true
    }

    private fun drawErrorState(): Unit = withBinding {
        progressBar.isGone = true
        scrollView.isVisible = true
        tvTextHistoryTitle.isGone = true
        recyclerView.isGone = true
        btnClearHistory.isGone = true

        with(ivErrorImage) {
            isVisible = true

            val imageResId =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    R.drawable.error_internet_connection_image_night_mode
                } else {
                    R.drawable.error_internet_connection_image_light_mode
                }

            setImageResource(imageResId)
        }

        with(tvErrorText) {
            isVisible = true
            setText(R.string.error_internet_connection_message)
        }

        btnRefresh.isVisible = true
    }

    private fun drawSearchResultState(searchedTrackList: List<Track>): Unit = withBinding {
        progressBar.isGone = true
        scrollView.isVisible = true
        tvTextHistoryTitle.isGone = true

        val searchResultIsNotEmpty = searchedTrackList.isNotEmpty()

        with(recyclerView) {
            isGone = !searchResultIsNotEmpty

            searchTrackListAdapter.trackList = searchedTrackList

            if (adapter != searchTrackListAdapter) {
                adapter = searchTrackListAdapter
            }
        }

        btnClearHistory.isGone = true

        with(ivErrorImage) {
            isGone = searchResultIsNotEmpty

            if (searchResultIsNotEmpty) return@with

            val imageResId =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    R.drawable.not_found_track_image_night_mode
                } else {
                    R.drawable.not_found_track_image_light_mode
                }

            setImageResource(imageResId)
        }

        with(tvErrorText) {
            isGone = searchResultIsNotEmpty

            if (searchResultIsNotEmpty) return@with

            setText(R.string.not_found_track_message)
        }

        btnRefresh.isGone = true
    }

    private inline fun <R> withBinding(action: ActivitySearchBinding.() -> R) = binding.action()
}
