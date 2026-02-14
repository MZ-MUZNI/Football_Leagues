package com.example.mobilecw2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
data class Leagues(
    @PrimaryKey() var leagueId: Int,
    var strLeague: String?,
    var strSport: String?,
    var strLeagueAlternate: String?
)
