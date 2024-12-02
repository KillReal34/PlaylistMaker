package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainActivityViewModel by viewModel()

    private val nightThemeObserver = Observer { isNightTheme: Boolean ->
        val mode = if (isNightTheme) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.libraryFragment -> showBottomNav()
                R.id.searchFragment -> showBottomNav()
                R.id.settingFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        viewModel.isNightThemeFlow.observeForever(nightThemeObserver)
    }

    private fun showBottomNav(){
        binding.bottomNavigationView.isVisible = true
    }

    private fun hideBottomNav(){
        binding.bottomNavigationView.isGone = true
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.isNightThemeFlow.removeObserver(nightThemeObserver)
    }
}