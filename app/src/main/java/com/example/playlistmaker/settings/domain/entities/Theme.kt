package com.example.playlistmaker.settings.domain.entities

enum class Theme {
    LIGHT,
    DARK;

    val isDark get() = this == DARK
}
