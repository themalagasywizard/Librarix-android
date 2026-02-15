package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SquareGrid2X2
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun LibraryScreen(
    books: List<SavedBook> = emptyList(),
    onBookClick: (SavedBook) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(Filter.ALL) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }

    val filteredBooks = remember(books, searchText, selectedFilter) {
        books
            .filter { book ->
                when (selectedFilter) {
                    Filter.ALL -> true
                    Filter.READING -> book.status == BookStatus.READING
                    Filter.FINISHED -> book.status == BookStatus.FINISHED
                    Filter.TBR -> book.status == BookStatus.WANT_TO_READ
                    Filter.FAVORITES -> book.isFavorite
                }
            }
            .filter { book ->
                if (searchText.isBlank()) true
                else {
                    book.title.contains(searchText, ignoreCase = true) ||
                            book.author.contains(searchText, ignoreCase = true) ||
                            (book.isbn?.contains(searchText, ignoreCase = true) ?: false)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Library",
                    fontWeight = FontWeight.Heavy,
                    fontSize = 32.sp,
                    color = Color.Black
                )

                Row {
                    IconButton(onClick = { viewMode = ViewMode.GRID }) {
                        Icon(
                            imageVector = Icons.Default.SquareGrid2X2,
                            contentDescription = "Grid",
                            tint = if (viewMode == ViewMode.GRID) LxPrimary else LxTextSecondary
                        )
                    }
                    IconButton(onClick = { viewMode = ViewMode.LIST }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "List",
                            tint = if (viewMode == ViewMode.LIST) LxPrimary else LxTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Search title, author, or ISBN...",
                        color = LxTextSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = LxTextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LxPrimary,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.12f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Filter.entries.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter.title,
                                fontWeight = if (selectedFilter == filter) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (selectedFilter == filter) Color.White else Color.Black.copy(alpha = 0.6f)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LxPrimary
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                }
            }
        }

        // Content
        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No saved books yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Use the + button to search Open Library and add books here.",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = LxTextSecondary
                    )
                }
            }
        } else {
            when (viewMode) {
                ViewMode.GRID -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
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
                        contentPadding = PaddingValues(16.dp),
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

@Composable
fun SavedBookGridCard(
    book: SavedBook,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(10.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
        ) {
            AsyncImage(
                model = book.coverURLString,
                contentDescription = book.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Status indicator
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        when (book.status) {
                            BookStatus.FINISHED -> Color.Green.copy(alpha = 0.9f)
                            BookStatus.READING -> Color(0xFFFF9800).copy(alpha = 0.9f)
                            BookStatus.WANT_TO_READ -> Color.Red.copy(alpha = 0.9f)
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Text(
            text = book.author,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = LxTextSecondary,
            maxLines = 1
        )
    }
}

@Composable
fun SavedBookRow(
    book: SavedBook,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LxSurfaceLight)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        AsyncImage(
            model = book.coverURLString,
            contentDescription = book.title,
            modifier = Modifier
                .width(64.dp)
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.author,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = LxTextSecondary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.status.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LxTextSecondary
                )
            }
        }
    }
}

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