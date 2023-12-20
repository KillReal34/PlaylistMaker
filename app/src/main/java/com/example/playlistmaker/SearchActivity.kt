package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_EDIT_TEXT = "text"
    }

    private lateinit var inputSearchText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button

    private val trackAdapter = TrackAdapter()

    var searchText = ""

    private val baseITunesUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseITunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val trackList = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearchText = findViewById(R.id.inputSearchText)
        clearButton = findViewById(R.id.clearIcon)
        backButton = findViewById(R.id.button_back_activitySearch)
        recyclerView = findViewById(R.id.recyclerView)
        errorImage = findViewById(R.id.errorImage)
        errorText = findViewById(R.id.errorText)
        refreshButton = findViewById(R.id.butRefresh)

        trackAdapter.track = trackList
        recyclerView.adapter = trackAdapter

        backButton.setOnClickListener{
            onBackPressed()
        }

        clearButton.setOnClickListener {
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            inputSearchText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            recyclerView.visibility = View.VISIBLE
            searchITunesApi(searchText)
        }

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchITunesApi(searchText)
                true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)
        inputSearchText.setText(searchText)
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

    private fun searchITunesApi(editText: String) {
        itunesService.search(editText).enqueue(object : Callback<TrackResponse>{
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.body()?.results?.isNotEmpty() == true) {
                    recyclerView.visibility = View.VISIBLE
                    trackList.clear()
                    trackList.addAll(response.body()?.results!!)
                    trackAdapter.notifyDataSetChanged()
                }
                else {
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    recyclerView.visibility = View.GONE
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        errorImage.setImageResource(R.drawable.not_found_track_image_night_mode)
                    }
                    else {
                        errorImage.setImageResource(R.drawable.not_found_track_image_light_mode)
                    }
                    errorText.setText(R.string.not_found_track_message)
                }
            }
            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                trackList.clear()
                recyclerView.visibility = View.GONE
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    errorImage.setImageResource(R.drawable.error_internet_connection_image_night_mode)
                } else {
                    errorImage.setImageResource(R.drawable.error_internet_connection_image_light_mode)
                }
                errorText.setText(R.string.error_internet_connection_message)
                refreshButton.visibility = View.VISIBLE
            }
        })
    }
}
