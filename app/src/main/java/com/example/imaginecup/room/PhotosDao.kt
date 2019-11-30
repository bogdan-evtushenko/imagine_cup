package com.example.imaginecup.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotosDao {

    @Query("SELECT * FROM photo")
    fun getAll(): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)
}
