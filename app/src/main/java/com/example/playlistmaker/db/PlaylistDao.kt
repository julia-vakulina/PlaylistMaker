package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)
    @Update
    fun updateNewPlaylist(playlist: PlaylistEntity)
    @Delete
    fun deletePlaylist(playList: PlaylistEntity)
    @Query("SELECT * FROM playlists_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE playlists_table set numberOfTracks = :trackCount WHERE id = :id")
    fun updatePlaylist(id:Int,  trackCount: Int)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>
}