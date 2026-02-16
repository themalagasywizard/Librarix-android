package com.librarix.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.librarix.data.remote.OpenLibraryDoc
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LocalIsDarkTheme
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderDark
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary
import com.librarix.presentation.viewmodel.AddBookViewModel
import com.librarix.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.UUID

// ============================================================
// Genre enum (matches iOS)
// ============================================================

private enum class Genre(val title: String) {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCI_FI("Sci-Fi"),
    FANTASY("Fantasy"),
    MYSTERY("Mystery")
}

// ============================================================
// Main Screen
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onDismiss: () -> Unit,
    onBookAdded: () -> Unit
) {
    val addBookViewModel = hiltViewModel<AddBookViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val uiState by addBookViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // Manual entry state
    var isManualEntryExpanded by remember { mutableStateOf(false) }
    var manualTitle by remember { mutableStateOf("") }
    var manualAuthor by remember { mutableStateOf("") }
    var manualPages by remember { mutableStateOf("") }
    var manualGenre by remember { mutableStateOf(Genre.FICTION) }
    var manualStatus by remember { mutableStateOf(BookStatus.READING) }
    var manualRating by remember { mutableIntStateOf(4) }
    var isSaving by remember { mutableStateOf(false) }

    // Bottom sheet for selected search result
    var selectedDoc by remember { mutableStateOf<OpenLibraryDoc?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (LocalIsDarkTheme.current) LxBackgroundDark else LxBackgroundLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Header ---
            Header(
                isManualEntryExpanded = isManualEntryExpanded,
                isSaving = isSaving,
                canSave = manualTitle.trim().isNotEmpty(),
                onCancel = onDismiss,
                onSave = {
                    val trimmedTitle = manualTitle.trim()
                    val trimmedAuthor = manualAuthor.trim()
                    if (trimmedTitle.isEmpty()) return@Header
                    isSaving = true
                    scope.launch {
                        val pageCount = manualPages.trim().toIntOrNull()
                        val book = SavedBook(
                            id = UUID.randomUUID().toString(),
                            title = trimmedTitle,
                            author = trimmedAuthor.ifEmpty { "Unknown author" },
                            description = null,
                            isbn = null,
                            pageCount = pageCount,
                            genre = manualGenre.title,
                            status = manualStatus,
                            rating = manualRating.toDouble(),
                            isFavorite = false,
                            openLibraryWorkKey = null,
                            coverURLString = null,
                            addedDate = System.currentTimeMillis()
                        )
                        homeViewModel.addBook(book)
                        isSaving = false
                        onBookAdded()
                    }
                }
            )

            // --- Scrollable Content ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Search section
                SearchSection(
                    searchQuery = uiState.searchQuery,
                    onQueryChanged = { addBookViewModel.onSearchQueryChanged(it) },
                    isSearching = uiState.isSearching,
                    errorMessage = uiState.errorMessage,
                    results = uiState.results,
                    onResultTapped = { doc -> selectedDoc = doc },
                    onScanBarcode = {
                        // TODO: Open barcode scanner
                    }
                )

                // Manual entry toggle
                ManualEntryToggle(
                    isExpanded = isManualEntryExpanded,
                    onToggle = { isManualEntryExpanded = !isManualEntryExpanded }
                )

                // Manual entry form (animated)
                AnimatedVisibility(
                    visible = isManualEntryExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        ManualEntryFields(
                            title = manualTitle,
                            onTitleChange = { manualTitle = it },
                            author = manualAuthor,
                            onAuthorChange = { manualAuthor = it },
                            pages = manualPages,
                            onPagesChange = { manualPages = it },
                            genre = manualGenre,
                            onGenreChange = { manualGenre = it }
                        )

                        StatusAndRatingSection(
                            status = manualStatus,
                            onStatusChange = { manualStatus = it },
                            rating = manualRating,
                            onRatingChange = { manualRating = it }
                        )

                        // Extra spacer for bottom CTA
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        }

        // --- Bottom CTA (only when manual entry is expanded) ---
        if (isManualEntryExpanded) {
            BottomCTA(
                isSaving = isSaving,
                enabled = manualTitle.trim().isNotEmpty() && !isSaving,
                modifier = Modifier.align(Alignment.BottomCenter),
                onAdd = {
                    val trimmedTitle = manualTitle.trim()
                    val trimmedAuthor = manualAuthor.trim()
                    if (trimmedTitle.isEmpty()) return@BottomCTA
                    isSaving = true
                    scope.launch {
                        val pageCount = manualPages.trim().toIntOrNull()
                        val book = SavedBook(
                            id = UUID.randomUUID().toString(),
                            title = trimmedTitle,
                            author = trimmedAuthor.ifEmpty { "Unknown author" },
                            description = null,
                            isbn = null,
                            pageCount = pageCount,
                            genre = manualGenre.title,
                            status = manualStatus,
                            rating = manualRating.toDouble(),
                            isFavorite = false,
                            openLibraryWorkKey = null,
                            coverURLString = null,
                            addedDate = System.currentTimeMillis()
                        )
                        homeViewModel.addBook(book)
                        isSaving = false
                        onBookAdded()
                    }
                }
            )
        }
    }

    // --- Selected Book Bottom Sheet ---
    if (selectedDoc != null) {
        val doc = selectedDoc!!
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { selectedDoc = null },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = if (LocalIsDarkTheme.current) LxBackgroundDark else LxBackgroundLight,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 4.dp)
                        .width(36.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(Color.Black.copy(alpha = 0.18f))
                )
            }
        ) {
            SelectedBookSheetContent(
                doc = doc,
                onAdd = { status ->
                    isSaving = true
                    scope.launch {
                        val description = addBookViewModel.fetchDescription(doc.key)
                        val book = SavedBook(
                            id = UUID.randomUUID().toString(),
                            title = doc.displayTitle,
                            author = doc.displayAuthor,
                            description = description,
                            isbn = doc.bestISBN,
                            pageCount = doc.numberOfPagesMedian,
                            genre = null,
                            status = status,
                            rating = null,
                            isFavorite = false,
                            openLibraryWorkKey = doc.key,
                            coverURLString = doc.coverUrl("L"),
                            addedDate = System.currentTimeMillis()
                        )
                        homeViewModel.addBook(book)
                        isSaving = false
                        selectedDoc = null
                        onBookAdded()
                    }
                },
                onBuy = {
                    // Build Amazon search URL
                    val query = "${doc.displayTitle} ${doc.displayAuthor}"
                    val encoded = URLEncoder.encode(query, "UTF-8")
                    "https://www.amazon.com/s?k=$encoded&i=stripbooks"
                }
            )
        }
    }
}

// ============================================================
// Header
// ============================================================

@Composable
private fun Header(
    isManualEntryExpanded: Boolean,
    isSaving: Boolean,
    canSave: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (LocalIsDarkTheme.current) LxBackgroundDark else LxBackgroundLight)
                .padding(horizontal = 16.dp)
                .padding(top = 18.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button
            Text(
                text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary,
                modifier = Modifier.clickable { onCancel() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Add New Book",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (LocalIsDarkTheme.current) Color.White else Color.Black.copy(alpha = 0.92f)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isManualEntryExpanded) {
                Text(
                    text = if (isSaving) "Saving\u2026" else "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canSave && !isSaving) LxPrimary else LxTextSecondary,
                    modifier = Modifier.clickable(enabled = canSave && !isSaving) { onSave() }
                )
            } else {
                // Invisible placeholder to keep title centered
                Spacer(modifier = Modifier.width(52.dp))
            }
        }

        // Bottom border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(if (LocalIsDarkTheme.current) LxBorderDark else LxBorderLight)
        )
    }
}

// ============================================================
// Search Section
// ============================================================

@Composable
private fun SearchSection(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    isSearching: Boolean,
    errorMessage: String?,
    results: List<OpenLibraryDoc>,
    onResultTapped: (OpenLibraryDoc) -> Unit,
    onScanBarcode: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Search bar
        SearchBar(query = searchQuery, onQueryChanged = onQueryChanged)

        // Status / Results
        if (isSearching) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = LxPrimary
                )
                Text(
                    text = "Searching Open Library\u2026",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = LxTextSecondary
                )
            }
        } else if (errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary
            )
        } else if (results.isNotEmpty()) {
            ResultsList(
                results = results.take(6),
                onResultTapped = onResultTapped
            )
        }

        // Quick Scan card
        QuickScanCard(onClick = onScanBarcode)
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = LxTextSecondary,
            modifier = Modifier.size(18.dp)
        )

        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .weight(1f)
                .height(24.dp),
            placeholder = {
                Text(
                    text = "Title, Author, or ISBN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = LxTextSecondary
                )
            },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.90f) else Color.Black.copy(alpha = 0.90f)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = LxPrimary
            )
        )
    }
}

// ============================================================
// Results List
// ============================================================

@Composable
private fun ResultsList(
    results: List<OpenLibraryDoc>,
    onResultTapped: (OpenLibraryDoc) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        results.forEach { doc ->
            SearchResultRow(doc = doc, onTap = { onResultTapped(doc) })
        }
    }
}

@Composable
private fun SearchResultRow(
    doc: OpenLibraryDoc,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (LocalIsDarkTheme.current) LxSurfaceDark else Color.White)
            .border(1.dp, if (LocalIsDarkTheme.current) LxBorderDark else LxBorderLight, RoundedCornerShape(14.dp))
            .clickable { onTap() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cover thumbnail
        val coverUrl = doc.coverUrl("M")
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(44.dp)
                    .height(66.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(66.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.25f),
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = doc.displayTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (LocalIsDarkTheme.current) Color.White else Color.Black.copy(alpha = 0.92f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = doc.displayAuthor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            tint = LxTextSecondary.copy(alpha = 0.8f),
            modifier = Modifier.size(14.dp)
        )
    }
}

// ============================================================
// Quick Scan Card (dark gradient, matches iOS)
// ============================================================

@Composable
private fun QuickScanCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        LxSurfaceDark,
                        Color(0xFF2C3A45)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Scan Barcode",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Auto-fill details instantly via camera",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = LxTextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(10.dp, CircleShape, ambientColor = LxPrimary.copy(alpha = 0.20f))
                    .clip(CircleShape)
                    .background(LxPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scan barcode",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ============================================================
// Manual Entry Toggle
// ============================================================

@Composable
private fun ManualEntryToggle(
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.05f))
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Manual entry (optional)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = LxTextSecondary
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = LxTextSecondary,
            modifier = Modifier.size(12.dp)
        )
    }
}

// ============================================================
// Manual Entry Fields
// ============================================================

@Composable
private fun ManualEntryFields(
    title: String,
    onTitleChange: (String) -> Unit,
    author: String,
    onAuthorChange: (String) -> Unit,
    pages: String,
    onPagesChange: (String) -> Unit,
    genre: Genre,
    onGenreChange: (Genre) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Cover placeholder + Title/Author side-by-side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cover upload placeholder
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(165.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))
                    .border(
                        width = 2.dp,
                        color = Color.Black.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = LxTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "ADD COVER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = LxTextSecondary
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LabeledField(
                    label = "TITLE",
                    value = title,
                    onValueChange = onTitleChange,
                    placeholder = "Book Title",
                    fontWeight = FontWeight.Medium
                )
                LabeledField(
                    label = "AUTHOR",
                    value = author,
                    onValueChange = onAuthorChange,
                    placeholder = "Author Name",
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Pages + Genre side-by-side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LabeledField(
                label = "PAGES",
                value = pages,
                onValueChange = onPagesChange,
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )

            GenrePicker(
                genre = genre,
                onGenreChange = onGenreChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontWeight: FontWeight = FontWeight.Normal,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.3.sp,
            color = LxTextSecondary,
            modifier = Modifier.padding(start = 4.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    color = LxTextSecondary
                )
            },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = fontWeight,
                color = if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.90f) else Color.Black.copy(alpha = 0.90f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f),
                unfocusedContainerColor = if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = LxPrimary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenrePicker(
    genre: Genre,
    onGenreChange: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "GENRE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.3.sp,
            color = LxTextSecondary,
            modifier = Modifier.padding(start = 4.dp)
        )

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))
                    .clickable { expanded = true }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = genre.title,
                    fontSize = 16.sp,
                    color = if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.90f) else Color.Black.copy(alpha = 0.90f),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = LxTextSecondary,
                    modifier = Modifier.size(12.dp)
                )
            }

            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Genre.entries.forEach { g ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(g.title) },
                        onClick = {
                            onGenreChange(g)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// ============================================================
// Status & Rating Section (card with segmented control + stars)
// ============================================================

@Composable
private fun StatusAndRatingSection(
    status: BookStatus,
    onStatusChange: (BookStatus) -> Unit,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Reading Status label
        Text(
            text = "READING STATUS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.3.sp,
            color = LxTextSecondary
        )

        // Segmented control
        StatusSegmentedControl(
            selected = status,
            onSelected = onStatusChange
        )

        // Your Rating header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "YOUR RATING",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.3.sp,
                color = LxTextSecondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$rating.0",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LxAccentGold
            )
        }

        // Star row
        StarRatingRow(
            rating = rating,
            onRatingChange = onRatingChange
        )

        Text(
            text = "Tap stars to rate",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = LxTextSecondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatusSegmentedControl(
    selected: BookStatus,
    onSelected: (BookStatus) -> Unit
) {
    val items = listOf(
        BookStatus.READING to BookStatus.READING.displayTitle,
        BookStatus.WANT_TO_READ to BookStatus.WANT_TO_READ.displayTitle,
        BookStatus.FINISHED to BookStatus.FINISHED.displayTitle
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (LocalIsDarkTheme.current) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))
            .padding(4.dp)
    ) {
        items.forEach { (status, label) ->
            val isSelected = selected == status
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .then(
                        if (isSelected) Modifier.background(LxPrimary)
                        else Modifier
                    )
                    .clickable { onSelected(status) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else LxTextSecondary
                )
            }
        }
    }
}

@Composable
private fun StarRatingRow(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 1..5) {
            val filled = rating >= i
            Icon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Rate $i stars",
                tint = if (filled) LxAccentGold else LxAccentGold.copy(alpha = 0.25f),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onRatingChange(i) }
            )
        }
    }
}

// ============================================================
// Bottom CTA Button
// ============================================================

@Composable
private fun BottomCTA(
    isSaving: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(if (LocalIsDarkTheme.current) LxBackgroundDark else LxBackgroundLight)
    ) {
        // Gradient fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = run {
                            val bg = if (LocalIsDarkTheme.current) LxBackgroundDark else LxBackgroundLight
                            listOf(bg.copy(alpha = 0f), bg.copy(alpha = 0.96f), bg)
                        }
                    )
                )
        )

        Button(
            onClick = onAdd,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp)
                .navigationBarsPadding()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LxPrimary,
                disabledContainerColor = LxPrimary.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 4.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isSaving) "Adding\u2026" else "Add to Library",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ============================================================
// Selected Book Bottom Sheet Content
// ============================================================

@Composable
private fun SelectedBookSheetContent(
    doc: OpenLibraryDoc,
    onAdd: (BookStatus) -> Unit,
    onBuy: () -> String
) {
    val context = LocalContext.current
    var selectedStatus by remember { mutableStateOf(BookStatus.WANT_TO_READ) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        // Cover image
        val coverUrl = doc.coverUrl("L")
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(130.dp)
                    .height(195.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(195.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.25f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title & Author
        Text(
            text = doc.displayTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (LocalIsDarkTheme.current) Color.White else Color.Black.copy(alpha = 0.92f),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = doc.displayAuthor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LxTextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Status picker + buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Reading Status label
            Text(
                text = "READING STATUS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.3.sp,
                color = LxTextSecondary
            )

            // Segmented control
            StatusSegmentedControl(
                selected = selectedStatus,
                onSelected = { selectedStatus = it }
            )

            // Add to Library button
            Button(
                onClick = { onAdd(selectedStatus) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LxPrimary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Add to Library",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Buy button
            Button(
                onClick = {
                    val url = onBuy()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(1.dp, if (LocalIsDarkTheme.current) LxBorderDark else LxBorderLight, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (LocalIsDarkTheme.current) LxSurfaceDark else Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = LxAccentGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Buy",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = LxAccentGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
