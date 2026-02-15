package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.domain.model.BookNote
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NoteEntry(
    val id: String,
    val text: String,
    val bookTitle: String,
    val bookAuthor: String,
    val createdAt: Long
)

@Composable
fun AllNotesLibraryScreen(
    notes: List<NoteEntry>,
    onNoteClick: (NoteEntry) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notes",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Done",
                color = LxPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onBackClick() }
            )
        }

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notes yet.",
                    color = LxTextSecondary
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onNoteClick(note) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = LxTextSecondary.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: NoteEntry,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(note.createdAt))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = note.text,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color.Black.copy(alpha = 0.88f),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = note.bookTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = LxPrimary,
                maxLines = 1
            )

            Text(
                text = " • ",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = LxTextSecondary.copy(alpha = 0.8f)
            )

            Text(
                text = dateString,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = LxTextSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ChevronRight,
                contentDescription = null,
                tint = LxTextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(11.dp)
            )
        }
    }
}
