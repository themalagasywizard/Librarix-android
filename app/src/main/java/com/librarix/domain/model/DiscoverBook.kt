package com.librarix.domain.model

data class DiscoverBook(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String? = null,
    val workKey: String? = null,
    val firstPublishYear: Int? = null,
    val pageCount: Int? = null,
    val isbns: List<String>? = null,
    val synopsis: String? = null
) {
    val isbn: String? get() = isbns?.firstOrNull()
}
