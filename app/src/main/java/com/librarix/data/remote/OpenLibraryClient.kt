package com.librarix.data.remote

import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.screens.DiscoverBook
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

data class OpenLibrarySearchResponse(
    val numFound: Int,
    val docs: List<OpenLibraryBook>
)

data class OpenLibraryBook(
    val key: String,
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?,
    val number_of_pages_median: Int?,
    val isbn: List<String>?,
    val subject: List<String>?
) {
    fun toCoverUrl(size: String = "M"): String? {
        return cover_i?.let { "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
    }
}

@Singleton
class OpenLibraryClient @Inject constructor(
    private val api: OpenLibraryApi
) {
    suspend fun searchBooks(query: String, limit: Int = 20): Result<List<DiscoverBook>> {
        return try {
            val response = api.search(
                q = query,
                limit = limit,
                fields = "key,title,author_name,first_publish_year,cover_i,number_of_pages_median,isbn,subject"
            )
            val books = response.docs.map { doc ->
                DiscoverBook(
                    key = doc.key,
                    title = doc.title,
                    author = doc.author_name?.firstOrNull() ?: "Unknown Author",
                    coverUrl = doc.toCoverUrl(),
                    firstPublishYear = doc.first_publish_year,
                    subject = doc.subject?.take(5)
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookDetails(workKey: String): Result<DiscoverBookDetail?> {
        return try {
            val key = workKey.removePrefix("/works/")
            val response = api.getWork(key)
            val book = response.docs?.firstOrNull()?.let { doc ->
                DiscoverBookDetail(
                    key = "/works/$key",
                    title = doc.title,
                    author = doc.author_name?.firstOrNull() ?: "Unknown Author",
                    coverUrl = doc.cover_i?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" },
                    firstPublishYear = doc.first_publish_year,
                    synopsis = doc.description?.value ?: doc.title,
                    pageCount = doc.number_of_pages_median?.toInt(),
                    subjects = doc.subject?.take(10)
                )
            }
            Result.success(book)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class DiscoverBookDetail(
    val key: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val synopsis: String,
    val pageCount: Int?,
    val subjects: List<String>?
)

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun search(
        @Query("q") q: String,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String
    ): OpenLibrarySearchResponse

    @GET("search.json")
    suspend fun getWork(
        @Query("q") q: String,
        @Query("limit") limit: Int = 1,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i,number_of_pages_median,description,subject"
    ): OpenLibrarySearchResponse
}