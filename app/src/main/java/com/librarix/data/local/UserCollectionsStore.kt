package com.librarix.data.local

import android.content.Context
import com.librarix.domain.model.CollectionMembership
import com.librarix.domain.model.SavedBook
import com.librarix.domain.model.UserCollection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCollectionsStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("librarix_collections", Context.MODE_PRIVATE)

    private val _collections = MutableStateFlow<List<UserCollection>>(emptyList())
    val collections: StateFlow<List<UserCollection>> = _collections.asStateFlow()

    private val memberships = mutableSetOf<CollectionMembership>()

    private val _collectionCountByBookId = MutableStateFlow<Map<String, Int>>(emptyMap())
    val collectionCountByBookId: StateFlow<Map<String, Int>> = _collectionCountByBookId.asStateFlow()

    init {
        loadLocal()
    }

    fun createCollection(title: String, description: String? = null) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val collection = UserCollection(
            id = UUID.randomUUID().toString(),
            title = trimmed,
            description = description,
            createdAt = System.currentTimeMillis()
        )
        _collections.value = listOf(collection) + _collections.value
        saveLocal()
    }

    fun deleteCollection(collectionId: String) {
        _collections.value = _collections.value.filter { it.id != collectionId }
        memberships.removeAll { it.collectionId == collectionId }
        saveLocal()
    }

    fun contains(bookId: String, collectionId: String): Boolean {
        return memberships.contains(CollectionMembership(collectionId = collectionId, bookLocalId = bookId))
    }

    fun toggle(bookId: String, collectionId: String) {
        val key = CollectionMembership(collectionId = collectionId, bookLocalId = bookId)
        if (memberships.contains(key)) {
            memberships.remove(key)
        } else {
            memberships.add(key)
        }
        updateCollectionCounts(bookId)
        saveLocal()
    }

    fun collectionCount(bookId: String): Int {
        return memberships.count { it.bookLocalId == bookId }
    }

    fun booksInCollection(collectionId: String, library: List<SavedBook>): List<SavedBook> {
        val bookIds = memberships
            .filter { it.collectionId == collectionId }
            .map { it.bookLocalId }
            .toSet()
        return library.filter { bookIds.contains(it.id) }
    }

    fun collectionsForBook(bookId: String): List<UserCollection> {
        val collectionIds = memberships
            .filter { it.bookLocalId == bookId }
            .map { it.collectionId }
            .toSet()
        return _collections.value.filter { collectionIds.contains(it.id) }
    }

    private fun updateCollectionCounts(bookId: String) {
        val count = memberships.count { it.bookLocalId == bookId }
        _collectionCountByBookId.value = _collectionCountByBookId.value + (bookId to count)
    }

    private fun loadLocal() {
        try {
            val json = prefs.getString("collections_cache", null) ?: return
            val obj = JSONObject(json)

            val collectionsArr = obj.optJSONArray("collections") ?: JSONArray()
            val collectionsList = mutableListOf<UserCollection>()
            for (i in 0 until collectionsArr.length()) {
                val c = collectionsArr.getJSONObject(i)
                collectionsList.add(UserCollection(
                    id = c.getString("id"),
                    userId = c.optString("userId").takeIf { it.isNotEmpty() },
                    title = c.getString("title"),
                    description = c.optString("description").takeIf { it.isNotEmpty() },
                    visibility = c.optString("visibility", "private"),
                    coverURLString = c.optString("coverURLString").takeIf { it.isNotEmpty() },
                    createdAt = c.optLong("createdAt", System.currentTimeMillis())
                ))
            }
            _collections.value = collectionsList

            val membershipsArr = obj.optJSONArray("memberships") ?: JSONArray()
            memberships.clear()
            for (i in 0 until membershipsArr.length()) {
                val m = membershipsArr.getJSONObject(i)
                memberships.add(CollectionMembership(
                    collectionId = m.getString("collectionId"),
                    bookLocalId = m.getString("bookLocalId")
                ))
            }
        } catch (_: Exception) {
            _collections.value = emptyList()
            memberships.clear()
        }
    }

    private fun saveLocal() {
        try {
            val collectionsArr = JSONArray()
            _collections.value.forEach { c ->
                collectionsArr.put(JSONObject().apply {
                    put("id", c.id)
                    c.userId?.let { put("userId", it) }
                    put("title", c.title)
                    c.description?.let { put("description", it) }
                    put("visibility", c.visibility)
                    c.coverURLString?.let { put("coverURLString", it) }
                    put("createdAt", c.createdAt)
                })
            }

            val membershipsArr = JSONArray()
            memberships.forEach { m ->
                membershipsArr.put(JSONObject().apply {
                    put("collectionId", m.collectionId)
                    put("bookLocalId", m.bookLocalId)
                })
            }

            val cache = JSONObject().apply {
                put("collections", collectionsArr)
                put("memberships", membershipsArr)
            }

            prefs.edit().putString("collections_cache", cache.toString()).apply()
        } catch (_: Exception) {
            // Ignore save failures
        }
    }
}
