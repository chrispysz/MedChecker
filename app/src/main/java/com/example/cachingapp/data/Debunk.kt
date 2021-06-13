package com.example.cachingapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "debunks")
data class Debunk(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var category: String,
    var question: String,
    var answer: String,
    var sources: String,
    var state: String
) : Parcelable
