package com.librarix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val coverURLString: String?,
    val pageCount: Int?,
    val currentPage: Int?,
    val progressFraction: Double?,
    val status: String,
    val genre: String?,
    val openLibraryWorkKey: String?,
    val isbn: String?,
    val lastProgressUpdate: Long?,
    val lastSessionNotes: String?,
    val notesJson: String?, // JSON serialized list of notes
    val isFavorite: Boolean,
    val finishedDate: Long?,
    val addedDate: Long?,
    val lastProgressDeltaPercent: Int?
)