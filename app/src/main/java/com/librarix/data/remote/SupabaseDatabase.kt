package com.librarix.data.remote

import com.librarix.domain.model.BookNote
import com.librarix.domain.model.SavedBook
import com.librarix.domain.model.UserCollection
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseDatabase @Inject constructor(
    private val supabaseAuthManager: SupabaseAuthManager
) {
    companion object {
        private const val SUPABASE_URL = "https://your-project.supabase.co"
        private const val SUPABASE_KEY = "your-anon-key"
    }

    private val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        auth {
            autoRefresh = true
        }
    }

    private val postgrest: Postgrest get() = client.postgrest

    // Books
    suspend fun syncBooks(books: List<SavedBook>) {
        val userId = supabaseAuthManager.getCurrentUser()?.id ?: return
        books.forEach { book ->
            postgrest.from("books").upsert(
                mapOf(
                    "id" to book.id,
                    "user_id" to userId,
                    "title" to book.title,
                    "author" to book.author,
                    "cover_url" to book.coverURLString,
                    "page_count" to book.pageCount,
                    "current_page" to book.currentPage,
                    "progress_fraction" to book.progressFraction,
                    "status" to book.status.name,
                    "open_library_key" to book.openLibraryWorkKey,
                    "isbn" to book.isbn,
                    "last_progress_update" to book.lastProgressUpdate,
                    "is_favorite" to book.isFavorite
                )
            )
        }
    }

    fun observeBooks(): Flow<List<SavedBook>> = callbackFlow {
        val userId = supabaseAuthManager.getCurrentUser()?.id ?: return@callbackFlow

        val channel = postgrest.channel("books")
        channel.postgresChangeFlow<PostgresChange>(
            filter = PostgresChangeFilter(
                schema = "public",
                table = "books",
                event = PostgresChangeEvent.ALL
            )
        ).collect { change ->
            // Reload books when change detected
            trySend(loadBooks(userId))
        }

        // Initial load
        trySend(loadBooks(userId))

        awaitClose {
            channel.unsubscribe()
        }
    }

    private suspend fun loadBooks(userId: String): List<SavedBook> {
        return try {
            postgrest.from("books")
                .select(columns = Columns.raw("*"))
                .eq("user_id", userId)
                .decodeList<Map<String, Any>>()
                .map { row ->
                    SavedBook(
                        id = row["id"] as String,
                        title = row["title"] as String,
                        author = row["author"] as String,
                        coverURLString = row["cover_url"] as? String,
                        pageCount = row["page_count"] as? Int,
                        currentPage = row["current_page"] as? Int,
                        progressFraction = row["progress_fraction"] as? Double,
                        status = com.librarix.domain.model.BookStatus.valueOf(row["status"] as String),
                        genre = row["genre"] as? String,
                        openLibraryWorkKey = row["open_library_key"] as? String,
                        isbn = row["isbn"] as? String,
                        lastProgressUpdate = row["last_progress_update"] as? Long,
                        lastSessionNotes = row["last_session_notes"] as? String,
                        notes = null,
                        isFavorite = row["is_favorite"] as Boolean,
                        finishedDate = row["finished_date"] as? Long,
                        addedDate = row["added_date"] as? Long,
                        lastProgressDeltaPercent = row["last_progress_delta_percent"] as? Int
                    )
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Collections
    suspend fun createCollection(name: String): UserCollection {
        val userId = supabaseAuthManager.getCurrentUser()?.id ?: throw Exception("Not authenticated")

        val result = postgrest.from("collections").insert(
            mapOf(
                "user_id" to userId,
                "name" to name
            )
        ).decodeSingle<Map<String, Any>>()

        return UserCollection(
            id = result["id"] as String,
            name = result["name"] as String,
            bookCount = 0
        )
    }

    suspend fun getCollections(): List<UserCollection> {
        val userId = supabaseAuthManager.getCurrentUser()?.id ?: return emptyList()

        return postgrest.from("collections")
            .select(columns = Columns.raw("*"))
            .eq("user_id", userId)
            .decodeList<Map<String, Any>>()
            .map { row ->
                UserCollection(
                    id = row["id"] as String,
                    name = row["name"] as String,
                    bookCount = 0
                )
            }
    }

    // Notes
    suspend fun addNote(bookId: String, note: BookNote) {
        val userId = supabaseAuthManager.getCurrentUser()?.id ?: return

        postgrest.from("notes").insert(
            mapOf(
                "id" to note.id,
                "user_id" to userId,
                "book_id" to bookId,
                "text" to note.text,
                "page_number" to note.pageNumber,
                "created_at" to note.createdAt
            )
        )
    }
}

data class PostgresChange(
    val commitTimestamp: String?,
    val record: Map<String, Any>?,
    val kind: String
)

data class PostgresChangeFilter(
    val schema: String,
    val table: String,
    val event: PostgresChangeEvent
)

enum class PostgresChangeEvent {
    ALL, INSERT, UPDATE, DELETE
}