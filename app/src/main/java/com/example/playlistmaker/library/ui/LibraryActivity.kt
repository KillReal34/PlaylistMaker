package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tracks = "tracks"
        val playlists = "playlists"

        binding.buttonBack.setOnClickListener{
            onBackPressed()
        }

        binding.viewPager.adapter =
            LibraryViewPagerAdapter(supportFragmentManager, lifecycle, tracks, playlists)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}