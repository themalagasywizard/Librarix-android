package com.librarix.data.remote

import com.librarix.domain.model.BookNote
import com.librarix.domain.model.SavedBook
import com.librarix.domain.model.UserCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stub implementation - replace with real Supabase integration
 * when credentials are configured.
 */
@Singleton
class SupabaseDatabase @Inject constructor(
    private val supabaseAuthManager: SupabaseAuthManager
) {
    suspend fun syncBooks(books: List<SavedBook>) {}

    fun observeBooks(): Flow<List<SavedBook>> = flowOf(emptyList())

    suspend fun createCollection(name: String): UserCollection {
        throw NotImplementedError("Supabase not configured")
    }

    suspend fun getCollections(): List<UserCollection> = emptyList()

    suspend fun addNote(bookId: String, note: BookNote) {}
}
