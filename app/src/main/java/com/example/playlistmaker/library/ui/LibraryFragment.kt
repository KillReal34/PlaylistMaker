package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    companion object{
        const val tracks = "tracks"
        const val playlists = "playlists"
    }

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE){
        FragmentLibraryBinding.inflate(layoutInflater)
    }

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val tracks = "tracks"
//        val playlists = "playlists"

        binding.viewPager.adapter =
            LibraryViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, tracks, playlists)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.library_activity_tablayout_favorite_tracks)
                1 -> tab.text = resources.getString(R.string.library_activity_tablayout_playlists)
            }
        }
        tabMediator?.attach()
    }

    override fun onDetach() {
        super.onDetach()
        tabMediator?.detach()
    }
}