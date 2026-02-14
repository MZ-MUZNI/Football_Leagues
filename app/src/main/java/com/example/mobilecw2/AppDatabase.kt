package com.example.mobilecw2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Leagues::class, Clubs::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getLeaguesDao(): LeaguesDao
    abstract fun getClubsDao(): ClubsDao
}