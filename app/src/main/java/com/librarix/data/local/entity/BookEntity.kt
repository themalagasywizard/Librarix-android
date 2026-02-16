package com.librarix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val coverURLString: String?,
    val pageCount: Int?,
    val currentPage: Int?,
    val progressFraction: Double?,
    val status: String,
    val genre: String?,
    val rating: Double?,
    val isFavorite: Boolean,
    val lastProgressDeltaPercent: Int?,
    val lastSessionMinutes: Int?,
    val lastSessionNotes: String?,
    val lastProgressUpdate: Long?,
    val finishedDate: Long?,
    val addedDate: Long?,
    val openLibraryWorkKey: String?,
    val isbn: String?,
    val remoteId: String?,
    val notesJson: String?
)
