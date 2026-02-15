package com.librarix.data.repository

import com.librarix.data.local.dao.BookDao
import com.librarix.data.local.entity.BookEntity
import com.librarix.domain.model.BookNote
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.viewmodel.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        openLibraryWorkKey = openLibraryWorkKey,
        isbn = isbn,
        lastProgressUpdate = lastProgressUpdate,
        lastSessionNotes = lastSessionNotes,
        notes = null, // Parse from JSON if needed
        isFavorite = isFavorite,
        finishedDate = finishedDate,
        addedDate = addedDate,
        lastProgressDeltaPercent = lastProgressDeltaPercent
    )
}

private fun SavedBook.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
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
        openLibraryWorkKey = openLibraryWorkKey,
        isbn = isbn,
        lastProgressUpdate = lastProgressUpdate,
        lastSessionNotes = lastSessionNotes,
        notesJson = null,
        isFavorite = isFavorite,
        finishedDate = finishedDate,
        addedDate = addedDate,
        lastProgressDeltaPercent = lastProgressDeltaPercent
    )
}