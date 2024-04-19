package com.example.playlistmaker.settings.data.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.playlistmaker.settings.domain.entities.Theme

class ThemeSharedPreferences(
    private val themeKey: String,
    private val sharedPreferences: SharedPreferences,
) : ThemePersistence {
    override fun set(newTheme: Theme) = sharedPreferences.edit {
        putBoolean(themeKey, newTheme.isDark)
    }

    override fun getLiveData(): LiveData<Theme> =
        object : LiveData<Theme>(sharedPreferences.getThemeValue(themeKey)) {
            private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

            override fun onActive() {
                super.onActive()

                if (listener == null) {
                    listener = provideListener(themeKey)
                }

                sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            }

            override fun onInactive() {
                super.onInactive()

                if (hasActiveObservers()) return

                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener ?: return)
            }

            private fun provideListener(
                themeKey: String,
            ) = object : SharedPreferences.OnSharedPreferenceChangeListener {
                override fun onSharedPreferenceChanged(
                    sharedPreferences: SharedPreferences?,
                    key: String?
                ) {
                    if (themeKey != key) return

                    postValue(sharedPreferences?.getThemeValue(themeKey) ?: Theme.LIGHT)
                }
            }
        }
}


private fun SharedPreferences.getThemeValue(themeKey: String) =
    if (getBoolean(themeKey, false)) Theme.DARK else Theme.LIGHT