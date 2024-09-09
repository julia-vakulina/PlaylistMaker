package com.example.playlistmaker.playlists.domain

import android.content.Context

class TrackStringGetter {
    fun getTrackString(number: Int): String {
        val mod10 = number % 10
        val mod100 = number % 100
        val track = "трек"
        val a = "а"
        val ov = "ов"
        return when {
            mod10 == 1 && mod100 != 11 -> "$number $track"
            mod10 in 2..4 && (mod100 !in 12..14) -> "$number $track$a"
            else -> "$number $track$ov"
        }
    }
}