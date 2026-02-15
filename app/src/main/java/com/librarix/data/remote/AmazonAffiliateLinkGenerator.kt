package com.librarix.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmazonAffiliateLinkGenerator @Inject constructor() {

    companion object {
        private const val AMAZON_BASE_URL = "https://www.amazon.com/dp/"
        private const val ASSOCIATE_TAG = "librarix-20" // Default associate tag
    }

    fun generateBuyLink(isbn: String?, title: String, author: String): String {
        if (!isbn.isNullOrBlank()) {
            return "$AMAZON_BASE_URL$isbn?tag=$ASSOCIATE_TAG"
        }

        // Fallback: search Amazon with title and author
        val query = "${title.replace(" ", "+")}+${author.replace(" ", "+")}"
        return "https://www.amazon.com/s?k=$query&tag=$ASSOCIATE_TAG"
    }

    fun generateSearchUrl(query: String): String {
        val encodedQuery = query.replace(" ", "+")
        return "https://www.amazon.com/s?k=$encodedQuery&tag=$ASSOCIATE_TAG"
    }
}