package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrack(tracksInPlaylistEntity: TrackInPlaylistEntity)

    @Query("SELECT SUM(trackId) FROM track_in_playlist_table WHERE trackId = :id GROUP BY trackId")
    fun isTrackInPlaylist(id: Int): Int
}