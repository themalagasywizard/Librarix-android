package com.librarix.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentlyReadingBook: SavedBook? = null,
    val finishedBooksThisYear: Int = 0,
    val yearlyGoal: Int = 12,
    val dayStreak: Int = 0,
    val pagesThisWeek: Int = 0,
    val recentBooks: List<SavedBook> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            libraryRepository.getAllBooks().collect { books ->
                val currentlyReading = books
                    .filter { it.status == BookStatus.READING }
                    .maxByOrNull { it.lastProgressUpdate ?: 0 }

                val finishedThisYear = books.count { book ->
                    book.status == BookStatus.FINISHED &&
                            book.finishedDate?.let { year ->
                                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) == year
                            } ?: false
                }

                _uiState.value = HomeUiState(
                    currentlyReadingBook = currentlyReading,
                    finishedBooksThisYear = finishedThisYear,
                    yearlyGoal = 12, // Default goal, could come from preferences
                    dayStreak = calculateDayStreak(books),
                    pagesThisWeek = books.sumOf { book ->
                        book.lastProgressDeltaPercent ?: 0
                    },
                    recentBooks = books.take(10),
                    isLoading = false
                )
            }
        }
    }

    private fun calculateDayStreak(books: List<SavedBook>): Int {
        // Simple implementation - check if there's activity in the last few days
        val recentActivity = books
            .mapNotNull { it.lastProgressUpdate }
            .maxOrNull() ?: return 0

        val daysSinceActivity = (System.currentTimeMillis() - recentActivity) / (24 * 60 * 60 * 1000)
        return if (daysSinceActivity <= 1) 1 else 0
    }

    fun updateProgress(book: SavedBook, newPage: Int) {
        viewModelScope.launch {
            val updated = book.copy(
                currentPage = newPage,
                lastProgressUpdate = System.currentTimeMillis()
            )
            libraryRepository.updateBook(updated)
        }
    }
}

interface LibraryRepository {
    fun getAllBooks(): kotlinx.coroutines.flow.Flow<List<SavedBook>>
    suspend fun updateBook(book: SavedBook)
    suspend fun addBook(book: SavedBook)
    suspend fun deleteBook(bookId: String)
}