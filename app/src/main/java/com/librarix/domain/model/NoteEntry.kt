package com.librarix.domain.model

data class NoteEntry(
    val id: String,
    val createdAt: Long,
    val text: String,
    val bookTitle: String,
    val bookAuthor: String
)
