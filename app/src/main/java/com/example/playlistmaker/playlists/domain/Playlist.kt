package com.example.playlistmaker.playlists.domain

import android.health.connect.ReadRecordsRequestUsingIds

data class Playlist(
    val playlistName: String,
    val playlistDescription: String?,
    val tracksIds: ArrayList<Int> = ArrayList(),
    val pathToImage: String,
    var numberOfTracks: Int = 0
)
