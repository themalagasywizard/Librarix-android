package com.librarix.domain.model

data class UserCollection(
    val id: String,
    val userId: String? = null,
    val title: String,
    val description: String? = null,
    val visibility: String = "private",
    val coverURLString: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class CollectionMembership(
    val collectionId: String,
    val bookLocalId: String
)
