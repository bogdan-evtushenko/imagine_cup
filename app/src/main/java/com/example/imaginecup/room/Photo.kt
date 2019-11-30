package com.example.imaginecup.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jsonData: String,
    val bitmap: Bitmap
)