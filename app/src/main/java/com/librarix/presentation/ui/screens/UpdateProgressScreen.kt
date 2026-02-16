package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LocalIsDarkTheme
import coil.compose.AsyncImage
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

@Composable
fun UpdateProgressView(
    book: SavedBook,
    onSave: (SavedBook) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPageText by remember {
        mutableStateOf((book.currentPage ?: 0).toString())
    }
    var sliderValue by remember {
        mutableFloatStateOf((book.currentPage ?: 0).toFloat())
    }
    var note by remember {
        mutableStateOf(book.lastSessionNotes ?: "")
    }
    var showingNote by remember { mutableStateOf(false) }

    val isDark = LocalIsDarkTheme.current
    val backgroundColor = if (isDark) LxBackgroundDark else LxBackgroundLight
    val surfaceColor = if (isDark) LxSurfaceDark else LxSurfaceLight
    val textColor = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    val maxPages = maxOf(book.pageCount ?: 0, 1)
    val currentPage = currentPageText.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 18.dp)
            .padding(top = 14.dp, bottom = 18.dp)
    ) {
        // Book context
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverURLString,
                contentDescription = book.title,
                modifier = Modifier
                    .size(width = 44.dp, height = 66.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )
                Text(
                    text = book.author,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LxTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress core
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Progress",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = LxTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = currentPageText,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        currentPageText = filtered
                        filtered.toIntOrNull()?.let { page ->
                            sliderValue = page.coerceIn(0, maxPages).toFloat()
                        }
                    },
                    modifier = Modifier.width(120.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 64.sp,
                        color = textColor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Text(
                    text = "of $maxPages pages",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = LxTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                        currentPageText = newValue.toInt().toString()
                    },
                    valueRange = 0f..maxPages.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = LxPrimary,
                        activeTrackColor = LxPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Note section
        Column {
            Button(
                onClick = { showingNote = !showingNote },
                colors = ButtonDefaults.textButtonColors()
            ) {
                Text(
                    text = "Add a note",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = LxTextSecondary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = LxTextSecondary
                )
            }

            if (showingNote) {
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceColor),
                    placeholder = {
                        Text(
                            "How was this session?",
                            color = LxTextSecondary
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save button
        Button(
            onClick = {
                val oldPercent = if (book.pageCount != null && book.pageCount > 0) {
                    ((book.currentPage ?: 0).toDouble() / book.pageCount * 100).toInt()
                } else {
                    ((book.progressFraction ?: 0.0) * 100).toInt()
                }
                val newPercent = (currentPage.toDouble() / maxPages * 100).toInt()
                val isComplete = book.pageCount != null && book.pageCount > 0 && currentPage >= book.pageCount

                val updated = book.copy(
                    currentPage = currentPage,
                    lastSessionNotes = note.ifBlank { null },
                    lastProgressUpdate = System.currentTimeMillis(),
                    lastProgressDeltaPercent = (newPercent - oldPercent).coerceIn(-100, 100),
                    progressFraction = null,
                    status = when {
                        isComplete -> BookStatus.FINISHED
                        book.status != BookStatus.FINISHED -> book.status
                        else -> BookStatus.READING
                    },
                    finishedDate = when {
                        isComplete -> book.finishedDate ?: System.currentTimeMillis()
                        else -> null
                    }
                )
                onSave(updated)
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = "Save",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}