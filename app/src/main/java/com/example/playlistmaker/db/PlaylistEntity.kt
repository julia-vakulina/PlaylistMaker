package com.example.playlistmaker.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playlistName: String,
    val tracksIds: String?,
    val playlistDescription: String?,
    val pathToImage: String,
    val numberOfTracks: Int = 0
)
