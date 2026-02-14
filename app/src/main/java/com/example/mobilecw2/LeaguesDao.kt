package com.example.mobilecw2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeaguesDao {
    @Query("SELECT * FROM leagues")
    suspend fun getAllLeagues(): List<Leagues>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(league: Leagues)
}