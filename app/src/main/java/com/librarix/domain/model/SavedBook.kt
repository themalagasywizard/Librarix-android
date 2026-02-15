package com.librarix.domain.model

data class SavedBook(
    val id: String,
    val title: String,
    val author: String,
    val coverURLString: String?,
    val pageCount: Int?,
    val currentPage: Int?,
    val progressFraction: Double?,
    val status: BookStatus,
    val genre: String?,
    val openLibraryWorkKey: String?,
    val isbn: String?,
    val lastProgressUpdate: Long?,
    val lastSessionNotes: String?,
    val notes: List<BookNote>?,
    val isFavorite: Boolean,
    val finishedDate: Long?,
    val addedDate: Long?,
    val lastProgressDeltaPercent: Int?
)

enum class BookStatus {
    WANT_TO_READ,
    READING,
    FINISHED
}

data class BookNote(
    val id: String,
    val text: String,
    val pageNumber: Int?,
    val createdAt: Long
)