package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle

@Composable
fun LibraryScreen(
    books: List<SavedBook> = emptyList(),
    onBookClick: (SavedBook) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(Filter.ALL) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }

    val filteredBooks = remember(books, searchText, selectedFilter) {
        val base = books.filter { book ->
            when (selectedFilter) {
                Filter.ALL -> true
                Filter.READING -> book.status == BookStatus.READING
                Filter.FINISHED -> book.status == BookStatus.FINISHED
                Filter.TBR -> book.status == BookStatus.WANT_TO_READ
                Filter.FAVORITES -> book.isFavorite
            }
        }
        val q = searchText.trim().lowercase()
        if (q.isEmpty()) base
        else base.filter {
            it.title.lowercase().contains(q) ||
                    it.author.lowercase().contains(q) ||
                    (it.isbn?.lowercase()?.contains(q) ?: false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        // Header (sticky, opaque, with bottom border)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LxBackgroundLight)
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 10.dp)
        ) {
            // Title row + view toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Library",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = Color.Black.copy(alpha = 0.92f)
                )

                // Segmented view toggle (matches iOS)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.06f))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ViewToggleButton(
                        isActive = viewMode == ViewMode.GRID,
                        iconText = "\u25A6", // grid icon placeholder
                        onClick = { viewMode = ViewMode.GRID }
                    )
                    ViewToggleButton(
                        isActive = viewMode == ViewMode.LIST,
                        iconText = "\u2630", // list icon placeholder
                        onClick = { viewMode = ViewMode.LIST }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search bar (matches iOS: HStack with icon + text field, surface bg, rounded border)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(LxSurfaceLight)
                    .border(1.dp, Color.Black.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "\uD83D\uDD0D",
                    fontSize = 16.sp,
                    color = LxTextSecondary
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Search title, author, or ISBN...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = LxTextSecondary
                        )
                    }
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.90f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter chips (horizontal scroll, matches iOS order and style)
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Filter.entries.forEach { filter ->
                    val isActive = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isActive) LxPrimary
                                else Color.Black.copy(alpha = 0.06f)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 18.dp)
                            .height(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter.title,
                            fontSize = 14.sp,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isActive) Color.White else Color.Black.copy(alpha = 0.60f)
                        )
                    }
                }
            }
        }

        // Bottom border line under header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LxBorderLight)
        )

        // Content
        if (filteredBooks.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 40.dp)
                ) {
                    Text(
                        text = "No saved books yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black.copy(alpha = 0.92f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Use the + button in the bottom nav to search Open Library and add books here.",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = LxTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            when (viewMode) {
                ViewMode.GRID -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 18.dp,
                            bottom = 18.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredBooks) { book ->
                            SavedBookGridCard(
                                book = book,
                                onClick = { onBookClick(book) }
                            )
                        }
                    }
                }

                ViewMode.LIST -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 18.dp,
                            bottom = 18.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredBooks) { book ->
                            SavedBookRow(
                                book = book,
                                onClick = { onBookClick(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// MARK: - View Toggle Button (matches iOS segmented control style)

@Composable
private fun ViewToggleButton(
    isActive: Boolean,
    iconText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) Color.White else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isActive) LxPrimary else Color.Black.copy(alpha = 0.45f)
        )
    }
}

// MARK: - Grid Card (matches iOS SavedBookGridCard)

@Composable
private fun SavedBookGridCard(
    book: SavedBook,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Cover image with 2:3 aspect ratio, shadow, and status dot
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Color.Black.copy(alpha = 0.14f),
                    spotColor = Color.Black.copy(alpha = 0.14f)
                )
                .clip(RoundedCornerShape(14.dp))
        ) {
            // Cover image or placeholder
            if (book.coverURLString != null) {
                AsyncImage(
                    model = book.coverURLString,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder matching iOS
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\uD83D\uDCD5",
                        fontSize = 18.sp,
                        color = Color.Black.copy(alpha = 0.25f)
                    )
                }
            }

            // Status indicator dot (top-right corner, matches iOS offset)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(18.dp)
                    .shadow(2.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.2f))
                    .clip(CircleShape)
                    .background(
                        when (book.status) {
                            BookStatus.READING -> Color(0xFFFF9800).copy(alpha = 0.9f) // orange
                            BookStatus.FINISHED -> Color.Green.copy(alpha = 0.9f) // green
                            BookStatus.WANT_TO_READ -> Color.Red.copy(alpha = 0.9f) // red
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(alpha = 0.92f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Author
        Text(
            text = book.author,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = LxTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// MARK: - List Row (matches iOS SavedBookRow)

@Composable
private fun SavedBookRow(
    book: SavedBook,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, LxBorderLight, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cover thumbnail
        if (book.coverURLString != null) {
            AsyncImage(
                model = book.coverURLString,
                contentDescription = book.title,
                modifier = Modifier
                    .width(64.dp)
                    .height(96.dp) // 64 * 1.5
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(96.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83D\uDCD5",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.25f)
                )
            }
        }

        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 0.92f)
            )

            Text(
                text = book.author,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = LxTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.status.displayTitle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LxTextSecondary
                )

                // Rating (if present, matches iOS star + value)
                if (book.rating != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(11.dp),
                            tint = LxAccentGold
                        )
                        Text(
                            text = String.format("%.1f", book.rating),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = LxTextSecondary
                        )
                    }
                }
            }
        }
    }
}

// MARK: - Enums

private enum class ViewMode {
    GRID, LIST
}

private enum class Filter(val title: String) {
    ALL("All"),
    TBR("TBR"),
    READING("Reading"),
    FINISHED("Read"),
    FAVORITES("Favourites")
}
