package com.librarix.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.librarix.presentation.ui.theme.LocalIsDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.librarix.domain.model.BookNote
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderDark
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookDetailScreen(
    book: SavedBook,
    onBackClick: () -> Unit,
    onUpdateProgress: () -> Unit,
    onAddToCollection: () -> Unit,
    onEditBook: () -> Unit,
    onDeleteBook: () -> Unit,
    onStatusChange: (BookStatus) -> Unit,
    onRatingChange: (Double) -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val backgroundColor = if (isDark) LxBackgroundDark else LxBackgroundLight
    val surfaceColor = if (isDark) LxSurfaceDark else LxSurfaceLight
    val borderColor = if (isDark) LxBorderDark else LxBorderLight
    val primaryText = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    val secondaryText = if (isDark) Color.White.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.45f)
    val bodyText = if (isDark) Color.White.copy(alpha = 0.75f) else Color.Black.copy(alpha = 0.70f)

    var showMenu by remember { mutableStateOf(false) }
    var synopsisExpanded by remember { mutableStateOf(false) }
    var userRating by remember { mutableDoubleStateOf(book.rating ?: 0.0) }
    var isBookmarked by remember { mutableStateOf(book.isFavorite) }
    var currentStatus by remember { mutableStateOf(book.status) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero section with blurred backdrop
            HeroSection(book = book, status = currentStatus, isDark = isDark, backgroundColor = backgroundColor)

            Spacer(modifier = Modifier.height(18.dp))

            // Title and meta
            TitleAndMeta(book = book, primaryText = primaryText)

            Spacer(modifier = Modifier.height(18.dp))

            // Main stack with cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Actions bar
                ActionsBar(
                    book = book,
                    currentStatus = currentStatus,
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    secondaryText = secondaryText,
                    onStatusChange = { status ->
                        currentStatus = status
                        onStatusChange(status)
                    },
                    onUpdateProgress = onUpdateProgress,
                    onAddToCollection = onAddToCollection
                )

                // User rating card
                UserRatingCard(
                    rating = userRating,
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    onRatingChange = {
                        userRating = it
                        onRatingChange(it)
                    },
                    onClearRating = {
                        userRating = 0.0
                        onRatingChange(0.0)
                    }
                )

                // Synopsis card
                SynopsisCard(
                    synopsis = book.description?.trim()?.ifEmpty { null }
                        ?: "No synopsis available yet.",
                    expanded = synopsisExpanded,
                    onToggleExpanded = { synopsisExpanded = !synopsisExpanded },
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    bodyText = bodyText
                )

                // Notes card
                NotesCard(
                    notes = book.notes ?: emptyList(),
                    onAddNote = { },
                    onViewAllNotes = { },
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    bodyText = bodyText,
                    backgroundColor = backgroundColor
                )

                // Author mini card
                AuthorMiniCard(
                    authorName = book.author,
                    onViewProfile = { },
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Top navigation bar
        TopNavBar(
            onBackClick = onBackClick,
            isBookmarked = isBookmarked,
            onToggleBookmark = {
                isBookmarked = !isBookmarked
                onToggleFavorite()
            },
            onShare = onShare,
            showMenu = showMenu,
            onShowMenu = { showMenu = it },
            onEditBook = {
                showMenu = false
                onEditBook()
            },
            onDeleteBook = {
                showMenu = false
                onDeleteBook()
            },
            isDark = isDark,
            backgroundColor = backgroundColor,
            primaryText = primaryText
        )
    }
}

// ─── Hero ────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(
    book: SavedBook,
    status: BookStatus,
    isDark: Boolean,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(352.dp)
    ) {
        // Blurred backdrop
        AsyncImage(
            model = book.coverURLString,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .alpha(if (isDark) 0.20f else 0.30f)
                .blur(radius = 30.dp)
                .scale(1.15f),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(352.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            backgroundColor.copy(alpha = 0.55f),
                            backgroundColor
                        )
                    )
                )
        )

        // Cover image with status badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(352.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 97.dp, bottom = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box {
                // Cover
                AsyncImage(
                    model = book.coverURLString,
                    contentDescription = book.title,
                    modifier = Modifier
                        .width(192.dp)
                        .height(317.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color.Black.copy(alpha = 0.50f),
                            spotColor = Color.Black.copy(alpha = 0.50f)
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                // Status badge overlaid on cover
                StatusBadge(
                    status = status,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-10).dp)
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: BookStatus, modifier: Modifier = Modifier) {
    Text(
        text = status.displayTitle.uppercase(),
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = Color(0xFF1E2B34),
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                ambientColor = Color.Black.copy(alpha = 0.15f)
            )
            .background(
                color = LxAccentGold,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

// ─── Title and Meta ──────────────────────────────────────────────────────────

@Composable
private fun TitleAndMeta(book: SavedBook, primaryText: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = primaryText,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            Text(
                text = book.author,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxPrimary,
                modifier = Modifier.clickable { }
            )

            Bullet()

            Text(
                text = "\u2014",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )

            Bullet()

            Text(
                text = if (book.pageCount != null) "${book.pageCount} Pages" else "Pages \u2014",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )
        }
    }
}

@Composable
private fun Bullet() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(4.dp)
            .background(
                color = LxTextSecondary.copy(alpha = 0.7f),
                shape = CircleShape
            )
    )
}

// ─── Actions Bar ─────────────────────────────────────────────────────────────

@Composable
private fun ActionsBar(
    book: SavedBook,
    currentStatus: BookStatus,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    secondaryText: Color,
    onStatusChange: (BookStatus) -> Unit,
    onUpdateProgress: () -> Unit,
    onAddToCollection: () -> Unit
) {
    val context = LocalContext.current
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Status dropdown pill
        Box(modifier = Modifier.weight(1f)) {
            ActionPill(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = LxPrimary.copy(alpha = 0.10f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = LxPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                title = currentStatus.displayTitle,
                modifier = Modifier.fillMaxWidth(),
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                secondaryText = secondaryText,
                onClick = { statusMenuExpanded = true }
            )

            DropdownMenu(
                expanded = statusMenuExpanded,
                onDismissRequest = { statusMenuExpanded = false }
            ) {
                BookStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(status.displayTitle)
                                if (currentStatus == status) {
                                    Text("\u2713", color = LxPrimary)
                                }
                            }
                        },
                        onClick = {
                            statusMenuExpanded = false
                            onStatusChange(status)
                        }
                    )
                }
            }
        }

        // Buy pill
        ActionPill(
            icon = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = secondaryText.copy(alpha = 0.10f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\uD83D\uDED2",
                        fontSize = 16.sp
                    )
                }
            },
            title = "Buy",
            modifier = Modifier.weight(1f),
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            secondaryText = secondaryText,
            onClick = {
                val query = URLEncoder.encode(
                    "${book.title} ${book.author}",
                    "UTF-8"
                )
                val url = "https://www.amazon.com/s?k=$query"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

        // Collection pill
        ActionPill(
            icon = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = secondaryText.copy(alpha = 0.10f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = if (isDark) Color.White.copy(alpha = 0.55f) else LxTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            title = "Collection",
            modifier = Modifier.weight(1f),
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            secondaryText = secondaryText,
            onClick = onAddToCollection
        )
    }
}

@Composable
private fun ActionPill(
    icon: @Composable () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    surfaceColor: Color,
    borderColor: Color,
    secondaryText: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        icon()

        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = secondaryText,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

// ─── User Rating Card ────────────────────────────────────────────────────────

@Composable
private fun UserRatingCard(
    rating: Double,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    onRatingChange: (Double) -> Unit,
    onClearRating: () -> Unit
) {
    CardContainer(surfaceColor = surfaceColor, borderColor = borderColor) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "YOUR RATING",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.4.sp,
                    color = LxTextSecondary
                )

                if (rating > 0) {
                    Text(
                        text = "${rating.toInt()} / 5",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LxTextSecondary
                    )
                } else {
                    Text(
                        text = "Tap a star",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = LxTextSecondary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in 1..5) {
                        val filled = rating >= i.toDouble()
                        Icon(
                            imageVector = if (filled) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Star $i",
                            tint = if (filled) LxAccentGold else LxTextSecondary.copy(alpha = 0.55f),
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { onRatingChange(i.toDouble()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (rating > 0) {
                    Text(
                        text = "Clear",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LxPrimary,
                        modifier = Modifier.clickable(onClick = onClearRating)
                    )
                }
            }
        }
    }
}

// ─── Synopsis Card ───────────────────────────────────────────────────────────

@Composable
private fun SynopsisCard(
    synopsis: String,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    bodyText: Color
) {
    CardContainer(surfaceColor = surfaceColor, borderColor = borderColor) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "SYNOPSIS",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.4.sp,
                color = LxTextSecondary
            )

            Text(
                text = synopsis,
                fontSize = 15.sp,
                color = bodyText,
                maxLines = if (expanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable(onClick = onToggleExpanded)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clickable(onClick = onToggleExpanded),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Show less" else "Read more",
                    tint = LxPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ─── Notes Card ──────────────────────────────────────────────────────────────

@Composable
private fun NotesCard(
    notes: List<BookNote>,
    onAddNote: () -> Unit,
    onViewAllNotes: () -> Unit,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    primaryText: Color,
    bodyText: Color,
    backgroundColor: Color
) {
    val dateFormat = remember {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    }

    CardContainer(surfaceColor = surfaceColor, borderColor = borderColor) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MY NOTES",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.4.sp,
                    color = LxTextSecondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View all",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LxPrimary,
                        modifier = Modifier.clickable(onClick = onViewAllNotes)
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = LxPrimary.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                            .clickable(onClick = onAddNote),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add note",
                            tint = LxPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Notes preview
            if (notes.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(backgroundColor)
                        .clickable(onClick = onAddNote)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = LxPrimary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = LxPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "No notes yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = primaryText
                        )
                        Text(
                            text = "Tap + to add your first note.",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = LxTextSecondary
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(backgroundColor)
                        .clickable(onClick = onViewAllNotes)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val sortedNotes = notes.sortedByDescending { it.createdAt }
                    val previewNotes = sortedNotes.take(2)

                    previewNotes.forEachIndexed { index, note ->
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = note.text,
                                fontSize = 13.sp,
                                color = bodyText,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = dateFormat.format(Date(note.createdAt)),
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = LxTextSecondary
                            )
                        }

                        if (index < previewNotes.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.alpha(0.35f)
                            )
                        }
                    }

                    if (notes.size > 2) {
                        Text(
                            text = "View all ${notes.size} notes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = LxPrimary
                        )
                    }
                }
            }
        }
    }
}

// ─── Author Mini Card ────────────────────────────────────────────────────────

@Composable
private fun AuthorMiniCard(
    authorName: String,
    onViewProfile: () -> Unit,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    CardContainer(surfaceColor = surfaceColor, borderColor = borderColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LxTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = authorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = primaryText
                )
                Text(
                    text = "Author",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LxTextSecondary
                )
            }

            Text(
                text = "View Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = LxPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(LxPrimary.copy(alpha = 0.10f))
                    .clickable(onClick = onViewProfile)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

// ─── Card Container ──────────────────────────────────────────────────────────

@Composable
private fun CardContainer(
    surfaceColor: Color,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
    ) {
        content()
    }
}

// ─── Top Navigation Bar ─────────────────────────────────────────────────────

@Composable
private fun TopNavBar(
    onBackClick: () -> Unit,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onShare: () -> Unit,
    showMenu: Boolean,
    onShowMenu: (Boolean) -> Unit,
    onEditBook: () -> Unit,
    onDeleteBook: () -> Unit,
    isDark: Boolean,
    backgroundColor: Color,
    primaryText: Color
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggleBookmark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (isBookmarked) LxAccentGold else primaryText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onShare),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = primaryText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { onShowMenu(true) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = primaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { onShowMenu(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Book") },
                            onClick = onEditBook
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Remove Book",
                                    color = Color.Red
                                )
                            },
                            onClick = onDeleteBook
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    if (isDark) Color.White.copy(alpha = 0.06f)
                    else Color.Black.copy(alpha = 0.06f)
                )
        )
    }
}
