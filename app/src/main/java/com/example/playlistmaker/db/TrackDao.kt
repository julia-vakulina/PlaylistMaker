package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timestamp DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT id FROM track_table")
    fun getIdTracks(): List<Long>
}