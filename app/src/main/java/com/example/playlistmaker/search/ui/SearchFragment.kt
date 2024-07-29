package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerActivity
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()
    private lateinit var editTextSearch: EditText
    private var searchText :String = ""
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        val clearHistory = binding.clearHistory
        searchHistoryLayout = binding.searchHistory
        val searchHistoryView = binding.searchHistoryView
        viewModel.getHistory()
        viewModel.searchHistoryVisible(false)
        searchHistoryView.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryView.adapter = adapterHistory

        adapterHistory.setTracks(viewModel.historyTracks)
        adapterHistory.notifyDataSetChanged()

        clearHistory.setOnClickListener {
            viewModel.clearHistory()
            searchHistoryLayout.visibility = View.GONE
        }

        val trackView = binding.trackView
        trackView.layoutManager = LinearLayoutManager(requireContext())
        trackView.adapter = adapter

        editTextSearch = binding.editTextSearch
        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editTextSearch.text.isEmpty() && viewModel.historyTracks.size!=0) {
                searchHistoryLayout.visibility = View.VISIBLE
            } else {
                searchHistoryLayout.visibility = View.GONE
            }
        }
        placeHolderNotFound = binding.placeHolderNotFound
        placeHolderNoConnect = binding.placeHolderNoConnect

        viewModel.getLoadingLiveData().observe(viewLifecycleOwner) {isLoading ->
            changeProgressBarVisibility(isLoading)
        }
        viewModel.getPlaceholderLiveData().observe(viewLifecycleOwner) {errorMessage ->
            changePlaceholderVisibility(errorMessage)
        }
        viewModel.getTracksLiveData().observe(viewLifecycleOwner) {tracks ->
            placeHolderNotFound.visibility = View.GONE
            placeHolderNoConnect.visibility = View.GONE
            adapter.setTracks(tracks)
            adapter.notifyDataSetChanged()
        }
        viewModel.getTracksHistoryLiveData().observe(viewLifecycleOwner) { _ ->
            adapterHistory.setTracks(viewModel.historyTracks)
            adapterHistory.notifyDataSetChanged()
        }
        viewModel.getSearchHistoryLiveData().observe(viewLifecycleOwner) {isVisible ->
            changeSearchHistoryVisibility(isVisible)
        }



        val buttonUpdate = binding.buttonUpdate
        fun searchTrack(text: String) {
            if (text.isNotEmpty()) {
                viewModel.searchTrack(text)

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
            placeHolderNotFound.visibility = View.GONE
        }

        val clearButton = binding.clearIcon
        clearButton.setOnClickListener {
            viewModel.getHistory()
            if (viewModel.historyTracks.size!=0) viewModel.searchHistoryVisible(true)
            else viewModel.searchHistoryVisible(false)
            viewModel.clear()
            editTextSearch.setText("")
            adapter.setTracks(ArrayList())
            adapter.notifyDataSetChanged()
            placeHolderNoConnect.visibility = View.GONE
            placeHolderNotFound.visibility = View.GONE
            viewModel.getHistory()
            adapterHistory.setTracks(viewModel.historyTracks)
            adapterHistory.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.toString().isEmpty()) viewModel.searchDebounce(s.toString())
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val newSearchText = savedInstanceState?.getString(SEARCH_TEXT_KEY, searchText)
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
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
        val gson = Gson()
        val json = gson.toJson(trackFromAPI)
        startActivity(playerIntent.putExtra(INTENT_KEY, json))

    }
    companion object {
        private const val SEARCH_TEXT_KEY = "search_text_key"
    }

}