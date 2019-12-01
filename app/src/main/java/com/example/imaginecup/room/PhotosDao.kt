package com.example.imaginecup.room

import androidx.room.*

@Dao
interface PhotosDao {

    @Query("SELECT * FROM photo")
    fun getAll(): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)

    @Delete
    fun delete(photo: Photo)
}
