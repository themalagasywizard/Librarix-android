package com.librarix.data.repository

import com.librarix.data.local.dao.BookDao
import com.librarix.data.local.entity.BookEntity
import com.librarix.domain.model.BookNote
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.viewmodel.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : LibraryRepository {

    override fun getAllBooks(): Flow<List<SavedBook>> {
        return bookDao.getAllBooks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateBook(book: SavedBook) {
        bookDao.updateBook(book.toEntity())
    }

    override suspend fun addBook(book: SavedBook) {
        bookDao.insertBook(book.toEntity())
    }

    override suspend fun getBookById(bookId: String): SavedBook? {
        return bookDao.getBookById(bookId)?.toDomain()
    }

    override suspend fun deleteBook(bookId: String) {
        bookDao.deleteBookById(bookId)
    }
}

// Mapper functions
private fun BookEntity.toDomain(): SavedBook {
    return SavedBook(
        id = id,
        title = title,
        author = author,
        description = description,
        coverURLString = coverURLString,
        pageCount = pageCount,
        currentPage = currentPage,
        progressFraction = progressFraction,
        status = when (status) {
            "READING" -> BookStatus.READING
            "FINISHED" -> BookStatus.FINISHED
            else -> BookStatus.WANT_TO_READ
        },
        genre = genre,
        rating = rating,
        isFavorite = isFavorite,
        lastProgressDeltaPercent = lastProgressDeltaPercent,
        lastSessionMinutes = lastSessionMinutes,
        lastSessionNotes = lastSessionNotes,
        lastProgressUpdate = lastProgressUpdate,
        finishedDate = finishedDate,
        addedDate = addedDate,
        notes = parseNotesJson(notesJson),
        openLibraryWorkKey = openLibraryWorkKey,
        isbn = isbn,
        remoteId = remoteId
    )
}

private fun SavedBook.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        description = description,
        coverURLString = coverURLString,
        pageCount = pageCount,
        currentPage = currentPage,
        progressFraction = progressFraction,
        status = when (status) {
            BookStatus.READING -> "READING"
            BookStatus.FINISHED -> "FINISHED"
            BookStatus.WANT_TO_READ -> "WANT_TO_READ"
        },
        genre = genre,
        rating = rating,
        isFavorite = isFavorite,
        lastProgressDeltaPercent = lastProgressDeltaPercent,
        lastSessionMinutes = lastSessionMinutes,
        lastSessionNotes = lastSessionNotes,
        lastProgressUpdate = lastProgressUpdate,
        finishedDate = finishedDate,
        addedDate = addedDate,
        openLibraryWorkKey = openLibraryWorkKey,
        isbn = isbn,
        remoteId = remoteId,
        notesJson = notesToJson(notes)
    )
}

private fun parseNotesJson(json: String?): List<BookNote>? {
    if (json.isNullOrBlank()) return null
    return try {
        val array = JSONArray(json)
        (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            BookNote(
                id = obj.getString("id"),
                text = obj.getString("text"),
                pageNumber = if (obj.has("pageNumber") && !obj.isNull("pageNumber")) obj.getInt("pageNumber") else null,
                createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                updatedAt = obj.optLong("updatedAt", obj.optLong("createdAt", System.currentTimeMillis()))
            )
        }
    } catch (e: Exception) {
        null
    }
}

private fun notesToJson(notes: List<BookNote>?): String? {
    if (notes.isNullOrEmpty()) return null
    val array = JSONArray()
    notes.forEach { note ->
        val obj = JSONObject().apply {
            put("id", note.id)
            put("text", note.text)
            note.pageNumber?.let { put("pageNumber", it) }
            put("createdAt", note.createdAt)
            put("updatedAt", note.updatedAt)
        }
        array.put(obj)
    }
    return array.toString()
}
