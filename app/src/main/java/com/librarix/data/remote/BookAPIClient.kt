package com.librarix.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

// ─── Response Models ────────────────────────────────────────────────────────────

@JsonClass(generateAdapter = true)
data class PickOfTheWeekResponse(
    @Json(name = "book") val book: TrendingBookData,
    @Json(name = "lastUpdated") val lastUpdated: String
)

@JsonClass(generateAdapter = true)
data class TrendingResponse(
    @Json(name = "books") val books: List<TrendingBookData>,
    @Json(name = "lastUpdated") val lastUpdated: String
)

@JsonClass(generateAdapter = true)
data class TopSellersResponse(
    @Json(name = "docs") val docs: List<TopSellerDoc>,
    @Json(name = "lastUpdated") val lastUpdated: String
)

@JsonClass(generateAdapter = true)
data class CollectionsResponse(
    @Json(name = "collections") val collections: List<CuratedCollection>,
    @Json(name = "lastUpdated") val lastUpdated: String
)

@JsonClass(generateAdapter = true)
data class APIHealth(
    @Json(name = "status") val status: String
)

// ─── Data Models ────────────────────────────────────────────────────────────────

@JsonClass(generateAdapter = true)
data class TrendingBookData(
    @Json(name = "rank") val rank: Int? = null,
    @Json(name = "title") val title: String,
    @Json(name = "subtitle") val subtitle: String? = null,
    @Json(name = "author") val author: String,
    @Json(name = "isbn") val isbn: String,
    @Json(name = "reason") val reason: String? = null,
    @Json(name = "synopsis") val synopsis: String? = null,
    @Json(name = "metadata") val metadata: BookAPIData? = null
) {
    val displayTitle: String
        get() = if (!subtitle.isNullOrBlank()) "$title: $subtitle" else title

    val displayAuthor: String
        get() = author

    val coverURL: String?
        get() = metadata?.coverImageUrl ?: metadata?.thumbnailUrl

    val pageCount: Int?
        get() = metadata?.pageCount
}

@JsonClass(generateAdapter = true)
data class BookAPIData(
    @Json(name = "isbn") val isbn: String,
    @Json(name = "title") val title: String? = null,
    @Json(name = "authors") val authors: List<String> = emptyList(),
    @Json(name = "description") val description: String? = null,
    @Json(name = "categories") val categories: List<String> = emptyList(),
    @Json(name = "pageCount") val pageCount: Int? = null,
    @Json(name = "publishedDate") val publishedDate: String? = null,
    @Json(name = "publisher") val publisher: PublisherData? = null,
    @Json(name = "coverImageUrl") val coverImageUrl: String? = null,
    @Json(name = "thumbnailUrl") val thumbnailUrl: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "previewLink") val previewLink: String? = null,
    @Json(name = "infoLink") val infoLink: String? = null,
    @Json(name = "sources") val sources: APISources = APISources()
)

@JsonClass(generateAdapter = true)
data class PublisherData(
    @Json(name = "name") val nameData: PublisherName? = null
) {
    val name: String? get() = nameData?.name
}

@JsonClass(generateAdapter = true)
data class PublisherName(
    @Json(name = "name") val name: String? = null
)

@JsonClass(generateAdapter = true)
data class APISources(
    @Json(name = "openLibrary") val openLibrary: Boolean = false,
    @Json(name = "googleBooks") val googleBooks: Boolean = false
)

@JsonClass(generateAdapter = true)
data class CuratedCollection(
    @Json(name = "id") val id: String? = null,
    @Json(name = "title") val name: String,
    @Json(name = "subtitle") val subtitle: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "curatedBy") val curatedBy: String? = null,
    @Json(name = "items") val books: List<CollectionBookItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class CollectionBookItem(
    @Json(name = "id") val isbn: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: String
) {
    val coverUrl: String
        get() = "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
}

@JsonClass(generateAdapter = true)
data class TopSellerDoc(
    @Json(name = "key") val key: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "author_name") val authorName: List<String>? = null,
    @Json(name = "cover_i") val coverId: Int? = null,
    @Json(name = "isbn") val isbn: List<String>? = null,
    @Json(name = "first_publish_year") val firstPublishYear: Int? = null,
    @Json(name = "number_of_pages_median") val numberOfPagesMedian: Int? = null
) {
    fun toCoverUrl(size: String = "M"): String? {
        coverId?.let { return "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
        isbn?.firstOrNull()?.let { return "https://covers.openlibrary.org/b/isbn/$it-$size.jpg" }
        return null
    }
}

// ─── Retrofit API Interface ─────────────────────────────────────────────────────

interface BookBrainApi {

    @GET("pick-of-the-week")
    suspend fun getPickOfTheWeek(): PickOfTheWeekResponse

    @GET("trending")
    suspend fun getTrending(): TrendingResponse

    @GET("top-sellers")
    suspend fun getTopSellers(): TopSellersResponse

    @GET("collections")
    suspend fun getCollections(): CollectionsResponse

    @GET("book")
    suspend fun getBook(@Query("isbn") isbn: String): BookAPIData

    @GET("health")
    suspend fun getHealth(): APIHealth
}

// ─── Client ─────────────────────────────────────────────────────────────────────

@Singleton
class BookAPIClient @Inject constructor(
    private val api: BookBrainApi
) {
    suspend fun getPickOfTheWeek(): Result<PickOfTheWeekResponse> {
        return try {
            Result.success(api.getPickOfTheWeek())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrending(): Result<TrendingResponse> {
        return try {
            Result.success(api.getTrending())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopSellers(): Result<TopSellersResponse> {
        return try {
            Result.success(api.getTopSellers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCollections(): Result<CollectionsResponse> {
        return try {
            Result.success(api.getCollections())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBook(isbn: String): Result<BookAPIData> {
        return try {
            Result.success(api.getBook(isbn))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHealth(): Result<APIHealth> {
        return try {
            Result.success(api.getHealth())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
