package com.example.imaginecup.room

import androidx.room.Room
import com.example.imaginecup.ICApp

object DatabaseClient {

    private val database by lazy {
        Room.databaseBuilder(ICApp.app, AppDatabase::class.java, "database")
            .allowMainThreadQueries()
            .build()
    }

    val photosDao by lazy { database.photosDao() }
}
