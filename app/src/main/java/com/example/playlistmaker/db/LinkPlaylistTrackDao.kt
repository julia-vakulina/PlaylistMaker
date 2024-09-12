package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LinkPlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLink(link: LinkPlaylistTrackEntity)

    @Delete
    fun deleteTrackFromPlaylist(linkPlaylistTrackEntity: LinkPlaylistTrackEntity)

    @Query("SELECT trackId FROM link_playlist_track_table WHERE playlistId = :playlistId")
    fun getTracksOfPlaylist(playlistId: Int): List<Int>

    @Query("SELECT playlistId FROM link_playlist_track_table WHERE trackId = :trackId AND playlistId = :playlistId")
    fun checkTrackInPlaylist(trackId: Int, playlistId: Int): Int

    @Query("SELECT COUNT(*) FROM link_playlist_track_table WHERE trackId = :trackId GROUP BY playlistId")
    fun isTrackInPlaylists(trackId: Int): Int
}