package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity (
    @PrimaryKey
    val id: Long,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Int,
    val previewUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)