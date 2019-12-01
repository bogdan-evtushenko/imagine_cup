package com.example.imaginecup.room

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jsonData: String,
    val bitmap: Bitmap
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        Converters().toBitmap(parcel.readString()!!)
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(id)
        p0?.writeString(jsonData)
        p0?.writeString(Converters().fromBitmap(bitmap))
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}