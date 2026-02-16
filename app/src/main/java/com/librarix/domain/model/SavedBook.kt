package com.librarix.domain.model

data class SavedBook(
    val id: String,
    val title: String,
    val author: String,
    val description: String? = null,
    val coverURLString: String? = null,
    val pageCount: Int? = null,
    val currentPage: Int? = null,
    val progressFraction: Double? = null,
    val status: BookStatus = BookStatus.READING,
    val genre: String? = null,
    val rating: Double? = null,
    val isFavorite: Boolean = false,
    val lastProgressDeltaPercent: Int? = null,
    val lastSessionMinutes: Int? = null,
    val lastSessionNotes: String? = null,
    val lastProgressUpdate: Long? = null,
    val finishedDate: Long? = null,
    val addedDate: Long? = null,
    val notes: List<BookNote>? = null,
    val openLibraryWorkKey: String? = null,
    val isbn: String? = null,
    val remoteId: String? = null
)

enum class BookStatus(val displayTitle: String) {
    WANT_TO_READ("TBR"),
    READING("Reading"),
    FINISHED("Read")
}

data class BookNote(
    val id: String,
    val text: String,
    val pageNumber: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
