package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.domain.SearchHistoryRepositoryImpl
import com.google.gson.Gson


private const val HISTORY_KEY = "history"
const val INTENT_KEY = "key"

class SearchActivity : AppCompatActivity() {

   //private val searchHistoryRepository by lazy {
   //     SearchHistoryRepositoryImpl(this)
   // }
    private lateinit var viewModel: SearchViewModel
    private lateinit var editTextSearch: EditText
    private var searchText :String = ""
    //private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressBar : ProgressBar
    private lateinit var placeHolderNotFound: LinearLayout
    private lateinit var placeHolderNoConnect: LinearLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private val adapter = TrackAdapter(
        object: TrackAdapter.TrackClickListener {
            override fun onTrackClick(trackFromAPI: TrackFromAPI) {
                viewModel.putToHistory(trackFromAPI)
                openPlayer(trackFromAPI)
            }
        }
    )
    private val adapterHistory = TrackAdapter(
        object: TrackAdapter.TrackClickListener {
            override fun onTrackClick(trackFromAPI: TrackFromAPI) {
                viewModel.putToHistory(trackFromAPI)
                openPlayer(trackFromAPI)
                viewModel.getHistory()
            }
        }
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
         searchHistoryLayout = findViewById<LinearLayout>(R.id.searchHistory)
        val searchHistoryView = findViewById<RecyclerView>(R.id.searchHistoryView)
        //val historyPrefs = searchHistoryRepository.getSearchHistory()
        //searchHistory = SearchHistory(historyPrefs)

        //searchHistory.getTracks()
        viewModel.getHistory()

        searchHistoryView.layoutManager = LinearLayoutManager(this)
        searchHistoryView.adapter = adapterHistory

        adapterHistory.setTracks(viewModel.historyTracks)
        adapterHistory.notifyDataSetChanged()

        clearHistory.setOnClickListener {
            //changeSearchHistoryVisibility(false)
            //searchHistory.clear()
            //viewModel.clearSearchHistory(false)
            viewModel.clearHistory()
            searchHistoryLayout.visibility = View.GONE
        }

        val trackView = findViewById<RecyclerView>(R.id.trackView)
        trackView.layoutManager = LinearLayoutManager(this)
        trackView.adapter = adapter

        editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editTextSearch.text.isEmpty() && viewModel.historyTracks.size!=0) {
                searchHistoryLayout.visibility = View.VISIBLE
            } else {
                searchHistoryLayout.visibility = View.GONE
            }
        }
        placeHolderNotFound = findViewById<LinearLayout>(R.id.placeHolderNotFound)
        placeHolderNoConnect = findViewById<LinearLayout>(R.id.placeHolderNoConnect)

        viewModel.getLoadingLiveData().observe(this) {isLoading ->
            changeProgressBarVisibility(isLoading)
        }
        viewModel.getPlaceholderLiveData().observe(this) {errorMessage ->
            changePlaceholderVisibility(errorMessage)
        }
        viewModel.getTracksLiveData().observe(this) {tracks ->
            placeHolderNotFound.visibility = View.GONE
            placeHolderNoConnect.visibility = View.GONE
            //trackView.adapter = adapter
            adapter.setTracks(tracks)
            adapter.notifyDataSetChanged()
        }
        viewModel.getTracksHistoryLiveData().observe(this) { _ ->
            adapterHistory.setTracks(viewModel.historyTracks)
            adapterHistory.notifyDataSetChanged()
        }
        viewModel.getSearchHistoryLiveData().observe(this) {isVisible ->
            changeSearchHistoryVisibility(isVisible)
        }



        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        fun searchTrack(text: String) {
            if (text.isNotEmpty()) {
                viewModel.searchTrack(text)

            }
        }
        //14
        val searchRunnable = Runnable { searchTrack(editTextSearch.text.toString()) }
        //fun searchDebounce(searchRunnable: Runnable) {
        //    handler.removeCallbacks(searchRunnable)
        //    handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        //}

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
            placeHolderNotFound.visibility = View.GONE
        }

        val buttonLeft = findViewById<Button>(R.id.button_left_search)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            viewModel.getHistory()
            if (viewModel.historyTracks.size!=0) viewModel.searchHistoryVisible(true)
            else viewModel.searchHistoryVisible(false)
            viewModel.clear()
            editTextSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            //trackView.adapter = TrackAdapter(listOf(), this)
            adapter.setTracks(ArrayList())
            adapter.notifyDataSetChanged()
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.GONE
            viewModel.getHistory()
            //searchHistoryView.adapter = TrackAdapter(viewModel.historyTracks, this)
            adapterHistory.setTracks(viewModel.historyTracks)
            adapterHistory.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //searchDebounce()

                viewModel.searchDebounce(searchRunnable)
                clearButton.visibility = clearButtonVisibility(s)
                if (editTextSearch.hasFocus() && s?.isEmpty() == false) {
                    searchHistoryLayout.visibility = View.GONE
                } else {
                    searchHistoryLayout.visibility = View.VISIBLE
                }
                if (start == 0) searchHistoryLayout.visibility = View.GONE
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
    private fun changeProgressBarVisibility(visible: Boolean) {
        if (visible == true) {progressBar.visibility = View.VISIBLE}
            else {progressBar.visibility = View.GONE}
    }
    private fun changePlaceholderVisibility(errorMessage: String) {
        if (errorMessage.equals("no internet")) {
            placeHolderNoConnect.visibility = View.VISIBLE
            placeHolderNotFound.visibility = View.GONE
        } else if (errorMessage.equals("not found")) {
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.VISIBLE
        } else {
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.GONE
        }
    }
    private fun changeSearchHistoryVisibility(visible: Boolean) {
        if (visible) {searchHistoryLayout.visibility = View.VISIBLE}
        else {searchHistoryLayout.visibility = View.GONE}
    }
    fun openPlayer(trackFromAPI: TrackFromAPI) {
        val playerIntent = Intent(this, PlayerActivity::class.java)
        val gson = Gson()
        val json = gson.toJson(trackFromAPI)
        startActivity(playerIntent.putExtra(INTENT_KEY, json))

    }
    companion object {
        private const val SEARCH_TEXT_KEY = "search_text_key"
    }



}