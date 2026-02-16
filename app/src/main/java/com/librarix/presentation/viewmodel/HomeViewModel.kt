package com.librarix.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarix.data.local.ReadingStatsStore
import com.librarix.domain.model.BookNote
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.NoteEntry
import com.librarix.domain.model.SavedBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class HomeUiState(
    val currentlyReadingBook: SavedBook? = null,
    val finishedBooksThisYear: Int = 0,
    val yearlyGoal: Int = 30,
    val dayStreak: Int = 0,
    val pagesThisWeek: Int = 0,
    val recentBooks: List<SavedBook> = emptyList(),
    val latestNotes: List<NoteEntry> = emptyList(),
    val allBooks: List<SavedBook> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val readingStatsStore: ReadingStatsStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            libraryRepository.getAllBooks().collect { books ->
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                val currentlyReading = books
                    .filter { it.status == BookStatus.READING }
                    .maxByOrNull { it.lastProgressUpdate ?: 0L }

                val finishedThisYear = books.count { book ->
                    book.status == BookStatus.FINISHED && book.finishedDate?.let { ts ->
                        val cal = Calendar.getInstance().apply { timeInMillis = ts }
                        cal.get(Calendar.YEAR) == currentYear
                    } ?: false
                }

                val latestNotes = books.flatMap { book ->
                    (book.notes ?: emptyList()).map { note ->
                        NoteEntry(
                            id = note.id,
                            createdAt = note.createdAt,
                            text = note.text,
                            bookTitle = book.title,
                            bookAuthor = book.author
                        )
                    }
                }.sortedByDescending { it.createdAt }.take(5)

                _uiState.value = HomeUiState(
                    currentlyReadingBook = currentlyReading,
                    finishedBooksThisYear = finishedThisYear,
                    yearlyGoal = readingStatsStore.goal(forYear = currentYear),
                    dayStreak = readingStatsStore.dayStreak(),
                    pagesThisWeek = readingStatsStore.pagesThisWeek(),
                    recentBooks = books.sortedByDescending { it.addedDate ?: 0L }.take(10),
                    latestNotes = latestNotes,
                    allBooks = books,
                    isLoading = false
                )
            }
        }
    }

    fun updateProgress(book: SavedBook, newPage: Int, notes: String? = null) {
        viewModelScope.launch {
            val oldPage = book.currentPage ?: 0
            val pagesDelta = newPage - oldPage

            val updated = book.copy(
                currentPage = newPage,
                progressFraction = book.pageCount?.let { if (it > 0) newPage.toDouble() / it else null },
                lastProgressUpdate = System.currentTimeMillis(),
                lastProgressDeltaPercent = book.pageCount?.let { if (it > 0) ((pagesDelta.toDouble() / it) * 100).toInt() else null },
                lastSessionNotes = notes,
                status = if (book.pageCount != null && newPage >= book.pageCount) BookStatus.FINISHED else book.status,
                finishedDate = if (book.pageCount != null && newPage >= book.pageCount) System.currentTimeMillis() else book.finishedDate
            )
            libraryRepository.updateBook(updated)

            if (pagesDelta > 0) {
                readingStatsStore.logSession(pagesDelta)
            }
        }
    }

    fun setYearlyGoal(goal: Int) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        readingStatsStore.setGoal(currentYear, goal)
        _uiState.value = _uiState.value.copy(yearlyGoal = goal)
    }

    fun addBook(book: SavedBook) {
        viewModelScope.launch {
            libraryRepository.addBook(book)
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            libraryRepository.deleteBook(bookId)
        }
    }
}

interface LibraryRepository {
    fun getAllBooks(): Flow<List<SavedBook>>
    suspend fun getBookById(bookId: String): SavedBook?
    suspend fun updateBook(book: SavedBook)
    suspend fun addBook(book: SavedBook)
    suspend fun deleteBook(bookId: String)
}
