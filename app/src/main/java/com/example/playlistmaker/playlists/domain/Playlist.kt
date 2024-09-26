package com.example.playlistmaker.playlists.domain

import android.health.connect.ReadRecordsRequestUsingIds
import java.io.Serializable

data class Playlist(
    val id: Int = 0,
    val playlistName: String,
    val playlistDescription: String?,
    val tracksIds: ArrayList<Int> = ArrayList(),
    val pathToImage: String,
    var numberOfTracks: Int = 0
) : Serializable
