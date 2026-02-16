package com.librarix.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarix.data.remote.BookAPIClient
import com.librarix.data.remote.CuratedCollection
import com.librarix.data.remote.OpenLibraryClient
import com.librarix.data.remote.OpenLibraryDoc
import com.librarix.data.remote.OpenLibrarySubjectWork
import com.librarix.data.remote.TrendingBookData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DiscoverChip(val title: String) {
    FOR_YOU("For You"),
    MY_COLLECTIONS("My Collections")
}

data class DiscoverUiState(
    val selectedChip: DiscoverChip = DiscoverChip.FOR_YOU,
    val pickOfTheWeek: TrendingBookData? = null,
    val trending: List<TrendingBookData> = emptyList(),
    val trendingWorks: List<OpenLibrarySubjectWork> = emptyList(),
    val collections: List<CuratedCollection> = emptyList(),
    val topSellers: List<OpenLibraryDoc> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<OpenLibraryDoc> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val openLibraryClient: OpenLibraryClient,
    private val bookAPIClient: BookAPIClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var searchJob: Job? = null
    private val subjectCache = mutableMapOf<String, List<OpenLibrarySubjectWork>>()

    init {
        load()
    }

    fun load(force: Boolean = false) {
        if (_uiState.value.selectedChip == DiscoverChip.MY_COLLECTIONS) {
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = null)
            return
        }

        if (!force && _uiState.value.trendingWorks.isNotEmpty()) return

        loadJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        loadJob = viewModelScope.launch {
            try {
                val slug = "fiction"
                val works = subjectCache[slug] ?: run {
                    val response = openLibraryClient.fetchSubjectWorks(slug, 18)
                    response.getOrThrow().works.also { subjectCache[slug] = it }
                }

                _uiState.value = _uiState.value.copy(
                    trendingWorks = works.drop(1).take(10),
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Couldn't load Discover right now."
                )
            }

            // Load additional data in parallel
            launch { loadPickOfTheWeek() }
            launch { loadTrendingBooks() }
            launch { loadCollections() }
            launch { loadTopSellers() }
        }
    }

    private suspend fun loadPickOfTheWeek() {
        if (_uiState.value.pickOfTheWeek != null) return
        bookAPIClient.getPickOfTheWeek().onSuccess { response ->
            _uiState.value = _uiState.value.copy(pickOfTheWeek = response.book)
        }
    }

    private suspend fun loadTrendingBooks() {
        if (_uiState.value.trending.isNotEmpty()) return
        bookAPIClient.getTrending().onSuccess { response ->
            _uiState.value = _uiState.value.copy(trending = response.books)
        }
    }

    private suspend fun loadCollections() {
        if (_uiState.value.collections.isNotEmpty()) return
        bookAPIClient.getCollections().onSuccess { response ->
            _uiState.value = _uiState.value.copy(collections = response.collections)
        }
    }

    private suspend fun loadTopSellers() {
        bookAPIClient.getTopSellers().onSuccess { response ->
            val docs = response.docs.map { seller ->
                OpenLibraryDoc(
                    key = seller.key ?: "",
                    title = seller.title,
                    authorName = seller.authorName,
                    coverI = seller.coverId,
                    isbn = seller.isbn,
                    firstPublishYear = seller.firstPublishYear,
                    numberOfPagesMedian = seller.numberOfPagesMedian
                )
            }
            _uiState.value = _uiState.value.copy(topSellers = docs)
        }
    }

    fun onChipChanged(chip: DiscoverChip) {
        _uiState.value = _uiState.value.copy(selectedChip = chip)
        load(force = true)
    }

    fun onSearchChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()

        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false)
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.value = _uiState.value.copy(isSearching = true)
            openLibraryClient.searchBooks(trimmed, 18).onSuccess { response ->
                _uiState.value = _uiState.value.copy(searchResults = response.docs, isSearching = false)
            }.onFailure {
                _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false)
            }
        }
    }
}
