package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.ui.FragmentAudioPlayer
import com.example.playlistmaker.player.ui.PlayerTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

class SearchFragment : Fragment() {
    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    companion object {
        private val SEARCH_DEBOUNCE_DELAY = 2.seconds
    }

    private val viewModel: SearchScreenViewModel by viewModel()

    private var searchJob: Job? = null

    private val searchTrackListAdapter = TrackAdapter(onTrackClick = ::onSearchedTackClick)

    private val historyTrackAdapter = TrackAdapter(onTrackClick = ::onHistoryTrackClick)

    private var inputSearchText: String?
        get() = withBinding { etInputSearchText.text?.toString() }
        set(value) = withBinding { etInputSearchText.setText(value) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {

            ivClearIcon.setOnClickListener { view ->
                inputSearchText = ""
                viewModel.setAuditionHistoryTrack()
            }

            btnRefresh.setOnClickListener { searchDebounce() }

            btnClearHistory.setOnClickListener { viewModel.clearAuditionHistory() }

            with(etInputSearchText) {
                setOnEditorActionListener { _, actionId, _ ->
                    val namePattern = inputSearchText

                    if (actionId == EditorInfo.IME_ACTION_DONE && namePattern?.isNotEmpty() == true) {
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
            etInputSearchText.textCursorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.cursor)
        }

        viewModel.screenStateLiveData.observe(viewLifecycleOwner) { state ->
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

    private fun searchDebounce() {
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY.inWholeMilliseconds)
            viewModel.searchTrackByName(namePattern = inputSearchText ?: return@launch)
        }
    }

    private fun onSearchTextChanged(newText: CharSequence?) {

        val searchIsNotEmpty = newText?.isNotEmpty() == true

        if (newText?.isNotEmpty() == true) {
            searchDebounce()
        } else {
            viewModel.setAuditionHistoryTrack()
        }

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

    private fun onSearchedTackClick(track: Track) {
        viewModel.addTrackToAuditionHistory(track = track)

        findNavController().navigate(R.id.action_searchFragment_to_fragmentAudioPlayer,
            FragmentAudioPlayer.createArgs(PlayerTrack(track = track)))
    }

    private fun onHistoryTrackClick(track: Track) =
        findNavController().navigate(R.id.action_searchFragment_to_fragmentAudioPlayer,
            FragmentAudioPlayer.createArgs(PlayerTrack(track = track)))

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

    private inline fun <R> withBinding(action: FragmentSearchBinding.() -> R) = binding.action()
}