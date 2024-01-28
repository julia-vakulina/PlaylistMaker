package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

   //

    private lateinit var editTextSearch: EditText
    private var searchText :String = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // спринт 10
        val trackView = findViewById<RecyclerView>(R.id.trackView)

        val trackList: ArrayList<Track> = ArrayList()

        val track1 = Track(
            trackName = "Smells Like Teen Spirit",
            artistName = "Nirvana",
            trackTime = "5:01",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        )
        val track2 = Track(
            trackName = "Billie Jean",
            artistName = "Michael Jackson",
            trackTime = "4:35",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        )
        val track3 = Track(
            trackName = "Stayin' Alive",
            artistName = "Bee Gees",
            trackTime = "4:10",
            artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        )
        val track4 = Track(
            trackName = "Whole Lotta Love",
            artistName = "Led Zeppelin",
            trackTime = "5:33",
            artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        )
        val track5 = Track(
            trackName = "Sweet Child O'Mine",
            artistName = "Guns N' Roses",
            trackTime = "5:03",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
        )

        trackList.add(track1)
        trackList.add(track2)
        trackList.add(track3)
        trackList.add(track4)
        trackList.add(track5)

        trackView.layoutManager = LinearLayoutManager(this)
        trackView.adapter = TrackAdapter(
            tracks = trackList
        )

        val buttonLeft = findViewById<Button>(R.id.button_left_search)
        buttonLeft.setOnClickListener {
            val leftIntent = Intent(this, MainActivity::class.java)
            startActivity(leftIntent)
        }
        editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            editTextSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
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
        const val SEARCH_TEXT_KEY = "1"
    }



}