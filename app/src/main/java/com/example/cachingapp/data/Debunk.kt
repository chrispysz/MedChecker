package com.example.cachingapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "debunks")
data class Debunk(
    @PrimaryKey val id: Int,
    val category: String,
    val question: String,
    val answer: String,
    val sources: String
): Parcelable