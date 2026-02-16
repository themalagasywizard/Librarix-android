package com.librarix.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarix.data.remote.OpenLibraryAuthor
import com.librarix.data.remote.OpenLibraryAuthorWork
import com.librarix.data.remote.OpenLibraryClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthorDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val authorKey: String? = null,
    val author: OpenLibraryAuthor? = null,
    val works: List<OpenLibraryAuthorWork> = emptyList()
)

@HiltViewModel
class AuthorDetailViewModel @Inject constructor(
    private val openLibraryClient: OpenLibraryClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorDetailUiState())
    val uiState: StateFlow<AuthorDetailUiState> = _uiState.asStateFlow()

    fun load(authorName: String, workKey: String?) {
        if (_uiState.value.isLoading) return
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val key = openLibraryClient.resolveAuthorKey(authorName, workKey).getOrNull()
                _uiState.value = _uiState.value.copy(authorKey = key)

                if (key == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Couldn't find an author profile on Open Library."
                    )
                    return@launch
                }

                val authorDeferred = async { openLibraryClient.fetchAuthor(key) }
                val worksDeferred = async { openLibraryClient.fetchAuthorWorks(key, 50) }

                val author = authorDeferred.await().getOrNull()
                val worksResponse = worksDeferred.await().getOrNull()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    author = author,
                    works = deduplicateWorks(worksResponse?.entries ?: emptyList())
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load author details."
                )
            }
        }
    }

    fun displayName(fallbackName: String): String {
        return _uiState.value.author?.name?.trim()?.takeIf { it.isNotEmpty() } ?: fallbackName
    }

    fun bioText(): String {
        return _uiState.value.author?.bioValue ?: "No biography available yet."
    }

    fun authorPhotoUrl(): String? {
        val key = _uiState.value.authorKey?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val olid = key.removePrefix("/authors/").replace("/", "").trim()
        if (olid.isEmpty()) return null
        return "https://covers.openlibrary.org/a/olid/$olid-L.jpg"
    }

    fun topWorks(): List<OpenLibraryAuthorWork> {
        return _uiState.value.works
            .sortedWith(compareByDescending<OpenLibraryAuthorWork> { it.covers?.isNotEmpty() == true }
                .thenBy { it.firstPublishYear?.take(4)?.toIntOrNull() ?: Int.MAX_VALUE }
                .thenBy { it.displayTitle })
            .take(8)
    }

    private fun deduplicateWorks(works: List<OpenLibraryAuthorWork>): List<OpenLibraryAuthorWork> {
        val bestByTitle = mutableMapOf<String, OpenLibraryAuthorWork>()
        for (work in works) {
            val normalized = normalizeTitle(work.displayTitle)
            if (normalized.isEmpty()) continue
            val existing = bestByTitle[normalized]
            if (existing == null) {
                bestByTitle[normalized] = work
            } else {
                bestByTitle[normalized] = chooseBetter(existing, work)
            }
        }
        return bestByTitle.values.toList()
    }

    private fun chooseBetter(existing: OpenLibraryAuthorWork, candidate: OpenLibraryAuthorWork): OpenLibraryAuthorWork {
        val eHasCover = existing.covers?.isNotEmpty() == true
        val cHasCover = candidate.covers?.isNotEmpty() == true
        if (eHasCover != cHasCover) return if (cHasCover) candidate else existing

        val eYear = existing.firstPublishYear?.take(4)?.toIntOrNull()
        val cYear = candidate.firstPublishYear?.take(4)?.toIntOrNull()
        if (eYear == null && cYear != null) return candidate
        if (eYear != null && cYear == null) return existing
        if (eYear != null && cYear != null && eYear != cYear) return if (cYear < eYear) candidate else existing

        if (candidate.displayTitle.length != existing.displayTitle.length) {
            return if (candidate.displayTitle.length < existing.displayTitle.length) candidate else existing
        }
        return if (candidate.key < existing.key) candidate else existing
    }

    private fun normalizeTitle(title: String): String {
        var t = title.trim().lowercase()
        val colonIdx = t.indexOf(':')
        if (colonIdx >= 0) t = t.substring(0, colonIdx)
        t = t.replace(Regex("\\([^)]*\\)"), "")
        t = t.replace(Regex("[^a-z0-9\\s]"), " ")
        t = t.trim().replace(Regex("\\s+"), " ")
        return t
    }
}
