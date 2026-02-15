package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Barcode
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.librarix.domain.model.BookStatus
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

data class AddBookSearchResult(
    val key: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val year: Int?
)

@Composable
fun AddBookScreen(
    onBackClick: () -> Unit,
    onBookSaved: () -> Unit,
    onShowScanner: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<AddBookSearchResult>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedBook by remember { mutableStateOf<AddBookSearchResult?>(null) }

    // Manual entry state
    var isManualEntry by remember { mutableStateOf(false) }
    var manualTitle by remember { mutableStateOf("") }
    var manualAuthor by remember { mutableStateOf("") }
    var manualPages by remember { mutableStateOf("") }
    var manualStatus by remember { mutableStateOf(BookStatus.READING) }
    var manualRating by remember { mutableStateOf(0f) }
    var isSaving by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
            .padding(top = 12.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = LxTextSecondary
                    )
                }

                Text(
                    text = "Add New Book",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                if (isManualEntry) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            isSaving = true
                            // TODO: Save book
                            isSaving = false
                            onBookSaved()
                        },
                        enabled = manualTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = if (isSaving) "Saving…" else "Save",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (manualTitle.isNotBlank()) LxPrimary else LxTextSecondary
                        )
                    }
                }
            }
        }

        if (!isManualEntry) {
            // Search Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            // TODO: Implement search
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Search by title, author, or ISBN...", color = LxTextSecondary)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = LxTextSecondary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = LxTextSecondary
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LxPrimary,
                            unfocusedBorderColor = LxBorderLight
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isSearching) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Searching Open Library…",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = LxTextSecondary
                            )
                        }
                    } else if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = Color.Red
                        )
                    }
                }
            }

            // Quick Scan Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .clickable { onShowScanner() },
                    colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(LxPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Barcode,
                                contentDescription = null,
                                tint = LxPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Scan Barcode",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Use your camera to scan ISBN",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = LxTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = LxTextSecondary
                        )
                    }
                }
            }

            // Toggle Manual Entry
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                        .clickable { isManualEntry = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Can't find it?",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = LxTextSecondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Enter manually",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = LxPrimary
                    )
                }
            }

            // Search Results
            if (searchResults.isNotEmpty()) {
                item {
                    Text(
                        text = "${searchResults.size} results",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = LxTextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(searchResults) { result ->
                    SearchResultItem(
                        result = result,
                        onClick = { selectedBook = result }
                    )
                }
            }
        } else {
            // Manual Entry Form
            item {
                ManualEntryForm(
                    title = manualTitle,
                    onTitleChange = { manualTitle = it },
                    author = manualAuthor,
                    onAuthorChange = { manualAuthor = it },
                    pages = manualPages,
                    onPagesChange = { manualPages = it },
                    status = manualStatus,
                    onStatusChange = { manualStatus = it },
                    rating = manualRating,
                    onRatingChange = { manualRating = it }
                )
            }
        }

        // Bottom spacer
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun SearchResultItem(
    result: AddBookSearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = result.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = result.author,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = LxTextSecondary,
                    maxLines = 1
                )

                result.year?.let { year ->
                    Text(
                        text = "$year",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = LxTextSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManualEntryForm(
    title: String,
    onTitleChange: (String) -> Unit,
    author: String,
    onAuthorChange: (String) -> Unit,
    pages: String,
    onPagesChange: (String) -> Unit,
    status: BookStatus,
    onStatusChange: (BookStatus) -> Unit,
    rating: Float,
    onRatingChange: (Float) -> Unit
) {
    var statusExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Title
        Text("TITLE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LxTextSecondary)
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Book title", color = LxTextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Author
        Text("AUTHOR", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LxTextSecondary)
        OutlinedTextField(
            value = author,
            onValueChange = onAuthorChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Author name", color = LxTextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pages
        Text("PAGE COUNT", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LxTextSecondary)
        OutlinedTextField(
            value = pages,
            onValueChange = onPagesChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Number of pages", color = LxTextSecondary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Status
        Text("STATUS", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LxTextSecondary)
        ExposedDropdownMenuBox(
            expanded = statusExpanded,
            onExpandedChange = { statusExpanded = it }
        ) {
            OutlinedTextField(
                value = status.name.lowercase().replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(14.dp)
            )
            ExposedDropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false }
            ) {
                BookStatus.entries.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            onStatusChange(s)
                            statusExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating
        Text("RATING", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LxTextSecondary)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Slider(
                value = rating,
                onValueChange = onRatingChange,
                valueRange = 0f..5f,
                steps = 4,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(thumbColor = LxAccentGold, activeTrackColor = LxAccentGold)
            )
            Text(
                text = "${rating.toInt()}/5",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}