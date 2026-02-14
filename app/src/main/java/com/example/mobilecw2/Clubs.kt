package com.example.mobilecw2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clubs")
data class Clubs(
    @PrimaryKey() var idTeam: String,
    var strTeam: String,
    var strTeamShort: String,
    var strAlternate: String,
    var intFormedYear: String,
    var strLeague: String,
    var idLeague: String,
    var strStadium: String,
    var strStadiumLocation: String,
    var intStadiumCapacity: String,
    var strWebsite: String,
    var strKeywords: String,
    var strStadiumThumb: String,
    var strTeamJersey: String,
    var strTeamLogo: String
)
