package com.librarix.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

// ============================================================
// Retrofit API Interface
// ============================================================

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun search(
        @Query("q") q: String,
        @Query("limit") limit: Int = 24,
        @Query("fields") fields: String = "key,title,author_name,cover_i,isbn,first_publish_year,number_of_pages_median"
    ): OpenLibrarySearchResponse

    @GET("works/{workId}.json")
    suspend fun getWork(@Path("workId") workId: String): OpenLibraryWorkDetail

    @GET("works/{workId}/editions.json")
    suspend fun getWorkEditions(
        @Path("workId") workId: String,
        @Query("limit") limit: Int = 50
    ): OpenLibraryEditionsResponse

    @GET("search/authors.json")
    suspend fun searchAuthors(
        @Query("q") q: String,
        @Query("limit") limit: Int = 10
    ): OpenLibraryAuthorSearchResponse

    @GET("authors/{authorId}.json")
    suspend fun getAuthor(@Path("authorId") authorId: String): OpenLibraryAuthor

    @GET("authors/{authorId}/works.json")
    suspend fun getAuthorWorks(
        @Path("authorId") authorId: String,
        @Query("limit") limit: Int = 50
    ): OpenLibraryAuthorWorksResponse

    @GET("subjects/{subject}.json")
    suspend fun getSubjectWorks(
        @Path("subject") subject: String,
        @Query("limit") limit: Int = 18
    ): OpenLibrarySubjectResponse

    @GET("api/books")
    suspend fun getBooksByISBN(
        @Query("bibkeys") bibkeys: String,
        @Query("format") format: String = "json",
        @Query("jscmd") jscmd: String = "data"
    ): Map<String, OpenLibraryISBNBook>
}

// ============================================================
// Client
// ============================================================

@Singleton
class OpenLibraryClient @Inject constructor(
    private val api: OpenLibraryApi
) {
    // Simple LRU cache using LinkedHashMap
    private val cache = object : LinkedHashMap<String, Any>(64, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Any>?): Boolean {
            return size > 128
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getCached(key: String): T? = synchronized(cache) { cache[key] as? T }

    private fun putCached(key: String, value: Any) = synchronized(cache) { cache[key] = value }

    suspend fun searchBooks(query: String, limit: Int = 24): Result<OpenLibrarySearchResponse> {
        val cacheKey = "search:$query:$limit"
        getCached<OpenLibrarySearchResponse>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.search(q = query, limit = limit)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchWorkDescription(workKey: String): Result<String?> {
        return try {
            val details = fetchWorkDetails(workKey).getOrThrow()
            Result.success(details.descriptionValue)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchWorkDetails(workKey: String): Result<OpenLibraryWorkDetail> {
        val workId = workKey.removePrefix("/works/")
        val cacheKey = "work:$workId"
        getCached<OpenLibraryWorkDetail>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.getWork(workId)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchWorkEditions(workKey: String, limit: Int = 50): Result<OpenLibraryEditionsResponse> {
        val workId = workKey.removePrefix("/works/")
        val cacheKey = "editions:$workId:$limit"
        getCached<OpenLibraryEditionsResponse>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.getWorkEditions(workId, limit)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchAuthors(query: String, limit: Int = 10): Result<OpenLibraryAuthorSearchResponse> {
        val cacheKey = "authorSearch:$query:$limit"
        getCached<OpenLibraryAuthorSearchResponse>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.searchAuthors(query, limit)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchAuthor(authorKey: String): Result<OpenLibraryAuthor> {
        val authorId = authorKey.removePrefix("/authors/")
        val cacheKey = "author:$authorId"
        getCached<OpenLibraryAuthor>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.getAuthor(authorId)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchAuthorWorks(authorKey: String, limit: Int = 50): Result<OpenLibraryAuthorWorksResponse> {
        val authorId = authorKey.removePrefix("/authors/")
        val cacheKey = "authorWorks:$authorId:$limit"
        getCached<OpenLibraryAuthorWorksResponse>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.getAuthorWorks(authorId, limit)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resolveAuthorKey(authorName: String, workKey: String? = null): Result<String?> {
        return try {
            // Try work details first for author key
            if (workKey != null) {
                val workResult = fetchWorkDetails(workKey)
                workResult.getOrNull()?.authors?.firstOrNull()?.author?.key?.let { key ->
                    return Result.success(key)
                }
            }
            // Fall back to author search
            val searchResult = searchAuthors(authorName, limit = 1)
            val key = searchResult.getOrNull()?.docs?.firstOrNull()?.key?.let { "/authors/$it" }
            Result.success(key)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchSubjectWorks(subject: String, limit: Int = 18): Result<OpenLibrarySubjectResponse> {
        val cacheKey = "subject:$subject:$limit"
        getCached<OpenLibrarySubjectResponse>(cacheKey)?.let { return Result.success(it) }
        return try {
            val response = api.getSubjectWorks(subject, limit)
            putCached(cacheKey, response)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchBooksByISBN(isbns: List<String>): Result<Map<String, OpenLibraryISBNBook>> {
        if (isbns.isEmpty()) return Result.success(emptyMap())
        return try {
            val result = coroutineScope {
                isbns.chunked(20).map { chunk ->
                    async {
                        val bibkeys = chunk.joinToString(",") { "ISBN:$it" }
                        api.getBooksByISBN(bibkeys = bibkeys)
                    }
                }.awaitAll()
            }
            // Merge all chunks and normalize keys (remove "ISBN:" prefix)
            val merged = mutableMapOf<String, OpenLibraryISBNBook>()
            for (map in result) {
                for ((key, value) in map) {
                    val normalizedKey = key.removePrefix("ISBN:")
                    merged[normalizedKey] = value
                }
            }
            Result.success(merged)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resolveWorkKey(isbn: String): Result<String?> {
        return try {
            val booksResult = fetchBooksByISBN(listOf(isbn)).getOrThrow()
            val workKey = booksResult.values.firstOrNull()?.workKey
            Result.success(workKey)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ============================================================
// Data Models
// ============================================================

// --- Search ---

@JsonClass(generateAdapter = true)
data class OpenLibrarySearchResponse(
    @Json(name = "numFound") val numFound: Int?,
    @Json(name = "docs") val docs: List<OpenLibraryDoc>
)

@JsonClass(generateAdapter = true)
data class OpenLibraryDoc(
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String?,
    @Json(name = "author_name") val authorName: List<String>?,
    @Json(name = "cover_i") val coverI: Int?,
    @Json(name = "isbn") val isbn: List<String>?,
    @Json(name = "first_publish_year") val firstPublishYear: Int?,
    @Json(name = "number_of_pages_median") val numberOfPagesMedian: Int?
) {
    val displayTitle: String
        get() = title?.trim()?.takeIf { it.isNotEmpty() } ?: "Untitled"
    val displayAuthor: String
        get() = authorName?.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() } ?: "Unknown author"
    val bestISBN: String?
        get() = isbn?.firstOrNull { it.trim().length == 13 } ?: isbn?.firstOrNull()

    fun coverUrl(size: String = "L"): String? {
        coverI?.let { return "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
        bestISBN?.let { return "https://covers.openlibrary.org/b/isbn/$it-$size.jpg" }
        return null
    }
}

// --- Work Detail ---

@JsonClass(generateAdapter = true)
data class OpenLibraryWorkDetail(
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: Any?,
    @Json(name = "covers") val covers: List<Int>?,
    @Json(name = "authors") val authors: List<OpenLibraryWorkAuthorRef>?
) {
    val descriptionValue: String?
        get() {
            return when (description) {
                is String -> (description as String).trim().takeIf { it.isNotEmpty() }
                is Map<*, *> -> ((description as Map<*, *>)["value"] as? String)?.trim()?.takeIf { it.isNotEmpty() }
                else -> null
            }
        }
}

@JsonClass(generateAdapter = true)
data class OpenLibraryWorkAuthorRef(
    @Json(name = "author") val author: OpenLibraryKeyRef?
)

@JsonClass(generateAdapter = true)
data class OpenLibraryKeyRef(
    @Json(name = "key") val key: String?
)

// --- Author Search ---

@JsonClass(generateAdapter = true)
data class OpenLibraryAuthorSearchResponse(
    @Json(name = "numFound") val numFound: Int?,
    @Json(name = "docs") val docs: List<OpenLibraryAuthorSearchDoc>
)

@JsonClass(generateAdapter = true)
data class OpenLibraryAuthorSearchDoc(
    @Json(name = "key") val key: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "top_work") val topWork: String?,
    @Json(name = "work_count") val workCount: Int?
)

// --- Author Profile ---

@JsonClass(generateAdapter = true)
data class OpenLibraryAuthor(
    @Json(name = "key") val key: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "bio") val bio: Any?,
    @Json(name = "photos") val photos: List<Int>?
) {
    val bioValue: String?
        get() {
            return when (bio) {
                is String -> (bio as String).trim().takeIf { it.isNotEmpty() }
                is Map<*, *> -> ((bio as Map<*, *>)["value"] as? String)?.trim()?.takeIf { it.isNotEmpty() }
                else -> null
            }
        }
}

// --- Author Works ---

@JsonClass(generateAdapter = true)
data class OpenLibraryAuthorWorksResponse(
    @Json(name = "size") val size: Int?,
    @Json(name = "entries") val entries: List<OpenLibraryAuthorWork>
)

@JsonClass(generateAdapter = true)
data class OpenLibraryAuthorWork(
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String?,
    @Json(name = "covers") val covers: List<Int>?,
    @Json(name = "first_publish_date") val firstPublishDate: String?
) {
    val displayTitle: String
        get() = title?.trim()?.takeIf { it.isNotEmpty() } ?: "Untitled"
    val firstPublishYear: String?
        get() = firstPublishDate?.trim()?.take(4)?.takeIf { it.isNotEmpty() }

    fun coverUrl(size: String = "L"): String? =
        covers?.firstOrNull()?.let { "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
}

// --- Subject Works ---

@JsonClass(generateAdapter = true)
data class OpenLibrarySubjectResponse(
    @Json(name = "name") val name: String?,
    @Json(name = "work_count") val workCount: Int?,
    @Json(name = "works") val works: List<OpenLibrarySubjectWork>
)

@JsonClass(generateAdapter = true)
data class OpenLibrarySubjectWork(
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String?,
    @Json(name = "cover_id") val coverID: Int?,
    @Json(name = "first_publish_year") val firstPublishYear: Int?,
    @Json(name = "authors") val authors: List<OpenLibrarySubjectAuthor>?
) {
    val displayTitle: String
        get() = title?.trim()?.takeIf { it.isNotEmpty() } ?: "Untitled"
    val displayAuthor: String
        get() = authors?.firstOrNull()?.name?.trim()?.takeIf { it.isNotEmpty() } ?: "Unknown author"

    fun coverUrl(size: String = "L"): String? =
        coverID?.let { "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
}

@JsonClass(generateAdapter = true)
data class OpenLibrarySubjectAuthor(
    @Json(name = "name") val name: String,
    @Json(name = "key") val key: String?
)

// --- ISBN /api/books ---

@JsonClass(generateAdapter = true)
data class OpenLibraryISBNBook(
    @Json(name = "title") val title: String?,
    @Json(name = "authors") val authors: List<OpenLibraryISBNAuthor>?,
    @Json(name = "cover") val cover: OpenLibraryISBNCover?,
    @Json(name = "key") val key: String?,
    @Json(name = "works") val works: List<OpenLibraryKeyRef>?,
    @Json(name = "number_of_pages") val numberOfPages: Int?,
    @Json(name = "publishers") val publishers: List<OpenLibraryISBNPublisher>?
) {
    val displayTitle: String
        get() = title?.trim()?.takeIf { it.isNotEmpty() } ?: "Untitled"
    val displayAuthor: String
        get() = authors?.firstOrNull()?.name?.trim()?.takeIf { it.isNotEmpty() } ?: "Unknown author"
    val workKey: String?
        get() = works?.firstOrNull()?.key?.trim()?.takeIf { it.isNotEmpty() }
            ?: key?.trim()?.takeIf { it.isNotEmpty() }
    val pageCount: Int?
        get() = numberOfPages

    fun coverUrl(preferLarge: Boolean = true): String? {
        if (preferLarge) cover?.large?.let { return it }
        cover?.medium?.let { return it }
        cover?.small?.let { return it }
        return null
    }
}

@JsonClass(generateAdapter = true)
data class OpenLibraryISBNAuthor(
    @Json(name = "name") val name: String,
    @Json(name = "key") val key: String?
)

@JsonClass(generateAdapter = true)
data class OpenLibraryISBNPublisher(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class OpenLibraryISBNCover(
    @Json(name = "small") val small: String?,
    @Json(name = "medium") val medium: String?,
    @Json(name = "large") val large: String?
)

// --- Editions ---

@JsonClass(generateAdapter = true)
data class OpenLibraryEditionsResponse(
    @Json(name = "size") val size: Int?,
    @Json(name = "entries") val entries: List<OpenLibraryEdition>
)

@JsonClass(generateAdapter = true)
data class OpenLibraryEdition(
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String?,
    @Json(name = "number_of_pages") val numberOfPages: Int?,
    @Json(name = "isbn_10") val isbn10: List<String>?,
    @Json(name = "isbn_13") val isbn13: List<String>?
)
