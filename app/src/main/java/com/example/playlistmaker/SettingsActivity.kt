package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackMain = findViewById<Button>(R.id.button_back)
        buttonBackMain.setOnClickListener{
            val backMainIntent = Intent(this, MainActivity::class.java)
            startActivity(backMainIntent)
        }
    }
}