package com.example.mobilecw2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClubsDao {
    @Query("SELECT * FROM clubs")
    suspend fun getAllClubs(): List<Clubs>

    @Query("SELECT * FROM clubs WHERE strTeam LIKE '%' || :keyword || '%' COLLATE NOCASE")
    suspend fun searchClubsByName(keyword: String): List<Clubs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClubs(clubs: MutableList<Clubs>)
}