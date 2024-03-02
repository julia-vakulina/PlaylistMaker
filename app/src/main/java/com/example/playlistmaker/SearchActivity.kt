package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val HISTORY_KEY = "history"

class SearchActivity : AppCompatActivity() {


    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesAPI::class.java)
    private lateinit var editTextSearch: EditText
    private var searchText :String = ""
    private lateinit var searchHistory: SearchHistory
    private val tracks = mutableListOf<TrackFromAPI>()

    override fun onStop() {
        super.onStop()
        searchHistory.putTracks()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //12
        val clearHistory = findViewById<Button>(R.id.clearHistory)
        val searchHistoryLayout = findViewById<LinearLayout>(R.id.searchHistory)
        val searchHistoryView = findViewById<RecyclerView>(R.id.searchHistoryView)
        val historyPrefs = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

        searchHistory = SearchHistory(historyPrefs)
        searchHistory.getTracks()
        val adapter = TrackAdapter(searchHistory.historyList, historyPrefs)

        searchHistoryView.adapter = adapter
        searchHistoryView.layoutManager = LinearLayoutManager(this)


        clearHistory.setOnClickListener {
            searchHistoryLayout.visibility = View.GONE
            historyPrefs.edit().clear().apply()
            searchHistory.historyList.clear()
            adapter.notifyDataSetChanged()
        }

        val trackView = findViewById<RecyclerView>(R.id.trackView)
        trackView.layoutManager = LinearLayoutManager(this)
        editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if (hasFocus && editTextSearch.text.isEmpty()) View.VISIBLE else View.GONE
        }

        val placeHolderNotFound = findViewById<LinearLayout>(R.id.placeHolderNotFound)
        val placeHolderNoConnect = findViewById<LinearLayout>(R.id.placeHolderNoConnect)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        // слушатель на едит текст
        fun searchTrack(text: String) {
            editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    itunesService.search(editTextSearch.text.toString())
                        .enqueue(object : Callback<TracksResponse> {
                            override fun onResponse(
                                call: Call<TracksResponse>,
                                response: Response<TracksResponse>){
                                if (response.code() == 200) {
                                    val tracks = response.body()?.results
                                    if (tracks?.isEmpty() == true)
                                        placeHolderNotFound.visibility = View.VISIBLE
                                    else trackView.adapter = TrackAdapter(tracks!!, historyPrefs)
                                }
                                else placeHolderNoConnect.visibility = View.VISIBLE

                            }
                            override fun onFailure(call: Call<TracksResponse>, t : Throwable){
                                placeHolderNoConnect.visibility = View.VISIBLE
                            }
                        })
                    true
                }
                false
            }
        }
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack(editTextSearch.text.toString())
                true
            }
            false
        }

        buttonUpdate.setOnClickListener() {
            searchTrack(editTextSearch.text.toString())
            placeHolderNoConnect.visibility = View.GONE
        }

        val buttonLeft = findViewById<Button>(R.id.button_left_search)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            editTextSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            trackView.adapter = TrackAdapter(listOf(), historyPrefs)
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.GONE
            adapter.notifyDataSetChanged()
            searchHistoryView.adapter = adapter
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (editTextSearch.hasFocus() && s?.isEmpty() == false) searchHistoryLayout.visibility = View.GONE
                else {
                    searchHistoryLayout.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        editTextSearch.addTextChangedListener(simpleTextWatcher)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val newSearchText = savedInstanceState.getString(SEARCH_TEXT_KEY, searchText)
        editTextSearch.setText(newSearchText)
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        private const val SEARCH_TEXT_KEY = "search_text_key"
    }



}