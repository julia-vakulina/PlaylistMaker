package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.domain.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.SearchViewModelFactory


private const val HISTORY_KEY = "history"
const val INTENT_KEY = "key"

class SearchActivity : AppCompatActivity() {

    private val searchHistoryRepository by lazy {
        SearchHistoryRepositoryImpl(this)
    }
    private lateinit var viewModel: SearchViewModel
    private lateinit var editTextSearch: EditText
    private var searchText :String = ""
    private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressBar : ProgressBar
    private lateinit var placeHolderNotFound: LinearLayout
    private lateinit var placeHolderNoConnect: LinearLayout
    private lateinit var searchHistoryLayout: LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
         searchHistoryLayout = findViewById<LinearLayout>(R.id.searchHistory)
        val searchHistoryView = findViewById<RecyclerView>(R.id.searchHistoryView)
        val historyPrefs = searchHistoryRepository.getSearchHistory()
        searchHistory = SearchHistory(historyPrefs)
        searchHistory.getTracks()

        searchHistoryView.adapter = TrackAdapter(searchHistory.historyList, historyPrefs)
        searchHistoryView.layoutManager = LinearLayoutManager(this)


        clearHistory.setOnClickListener {
            //changeSearchHistoryVisisbility(false)
            searchHistory.clear()
            viewModel.clearSearchHistory(false)
        }

        val trackView = findViewById<RecyclerView>(R.id.trackView)
        trackView.layoutManager = LinearLayoutManager(this)
        editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editTextSearch.text.isEmpty() && searchHistory.historyList.size!=0) {
                searchHistoryLayout.visibility = View.VISIBLE
                viewModel.clearSearchHistory(true)
            } else {
                searchHistoryLayout.visibility = View.GONE
                viewModel.clearSearchHistory(false)
            }
        }
        placeHolderNotFound = findViewById<LinearLayout>(R.id.placeHolderNotFound)
        placeHolderNoConnect = findViewById<LinearLayout>(R.id.placeHolderNoConnect)

        viewModel = ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]
        viewModel.getLoadingLiveData().observe(this) {isLoading ->
            changeProgressBarVisibility(isLoading)
        }
        viewModel.getPlaceholderLiveData().observe(this) {errorMessage ->
            changePlaceholderVisibility(errorMessage)
        }
        viewModel.getSearchHistoryLiveData().observe(this) {isClear ->
            changeSearchHistoryVisibility(isClear)
        }
        viewModel.getTracksLiveData().observe(this) {tracks ->
            placeHolderNotFound.visibility = View.GONE
            placeHolderNoConnect.visibility = View.GONE
            trackView.adapter = TrackAdapter(tracks, historyPrefs)
        }


        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        // слушатель на едит текст
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
            viewModel.clear()
            editTextSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
            trackView.adapter = TrackAdapter(listOf(), historyPrefs)
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.GONE
            searchHistory.getTracks()
            searchHistoryView.adapter = TrackAdapter(searchHistory.historyList, historyPrefs)
            if (searchHistory.historyList.size==0) searchHistoryLayout.visibility = View.GONE

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //searchDebounce()
                viewModel.searchDebounce(searchRunnable)
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
    companion object {
        private const val SEARCH_TEXT_KEY = "search_text_key"
    }



}