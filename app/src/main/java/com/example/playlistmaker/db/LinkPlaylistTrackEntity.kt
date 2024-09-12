package com.example.playlistmaker.db

import androidx.room.Entity

@Entity(
    tableName = "link_playlist_track_table",
    primaryKeys = ["playlistId", "trackId"]
)
class LinkPlaylistTrackEntity (
    val playlistId: Int,
    val trackId: Int
)