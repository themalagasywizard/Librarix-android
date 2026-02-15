package com.librarix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.librarix.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY lastProgressUpdate DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = 'READING' ORDER BY lastProgressUpdate DESC")
    fun getCurrentlyReadingBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = 'FINISHED' ORDER BY finishedDate DESC")
    fun getFinishedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: String)

    @Query("SELECT COUNT(*) FROM books WHERE status = 'FINISHED'")
    fun getFinishedBooksCount(): Flow<Int>

    @Query("SELECT SUM(currentPage) FROM books")
    fun getTotalPagesRead(): Flow<Int?>
}