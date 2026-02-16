package com.librarix.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarix.data.remote.OpenLibraryClient
import com.librarix.data.remote.OpenLibraryDoc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBookUiState(
    val searchQuery: String = "",
    val results: List<OpenLibraryDoc> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val openLibraryClient: OpenLibraryClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()

        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), isSearching = false, errorMessage = null)
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.value = _uiState.value.copy(isSearching = true, errorMessage = null)
            openLibraryClient.searchBooks(trimmed, 20).onSuccess { response ->
                _uiState.value = _uiState.value.copy(results = response.docs, isSearching = false)
            }.onFailure {
                _uiState.value = _uiState.value.copy(results = emptyList(), isSearching = false, errorMessage = "Couldn't search Open Library. Try again.")
            }
        }
    }

    suspend fun fetchDescription(workKey: String?): String? {
        if (workKey == null) return null
        return openLibraryClient.fetchWorkDescription(workKey).getOrNull()
    }
}
