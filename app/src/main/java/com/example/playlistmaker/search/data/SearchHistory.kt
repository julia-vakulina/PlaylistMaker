package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

 const val HISTORY_KEY = "history"
 const val HISTORY_SIZE = 10

class SearchHistory (private val sharedPreferences: SharedPreferences) {
    var historyList = mutableListOf<TrackFromAPI>()

    fun clear() {
        sharedPreferences.edit().clear().apply()
        historyList = mutableListOf<TrackFromAPI>()
    }
    fun getTracks(): List<TrackFromAPI> {
        val s = sharedPreferences.getString(HISTORY_KEY, null)
        historyList = listFromJson(s)
        return historyList
    }
    fun putTracks(){
        val s = jsonFromList(historyList)
        sharedPreferences.edit().putString(HISTORY_KEY, s).apply()
    }
    fun jsonFromList(list: MutableList<TrackFromAPI>): String{
        val gson = Gson()
        return gson.toJson(list)
    }
    fun listFromJson(json: String?): MutableList<TrackFromAPI>{
        val gson = Gson()
        val listType = object : TypeToken<MutableList<TrackFromAPI>>() {}.type
        return gson.fromJson(json, listType) ?: mutableListOf()
    }

    fun addTrack(track: TrackFromAPI){
        getTracks()
        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        if (historyList.size >= HISTORY_SIZE){
            historyList.removeAt(HISTORY_SIZE -1)
        }
        historyList.add(0, track)
        putTracks()
    }
}