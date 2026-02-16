package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import com.librarix.presentation.ui.theme.LocalIsDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.NoteEntry
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
import com.librarix.presentation.viewmodel.HomeUiState
import com.librarix.presentation.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ---------------------------------------------------------------------------
// Home Screen - matches iOS HomeView layout
// ---------------------------------------------------------------------------

@Composable
fun HomeScreen(
    onViewAllClick: () -> Unit,
    onBookClick: (SavedBook) -> Unit,
    onUpdateProgress: (SavedBook) -> Unit
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    var showBooksReadSheet by remember { mutableStateOf(false) }
    var showGoalEditSheet by remember { mutableStateOf(false) }

    val isDark = LocalIsDarkTheme.current
    val backgroundColor = if (isDark) LxBackgroundDark else LxBackgroundLight
    val primaryText = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    val secondaryText = if (isDark) Color.White.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.45f)
    val surfaceColor = if (isDark) LxSurfaceDark else LxSurfaceLight
    val borderColor = if (isDark) LxBorderDark else LxBorderLight
    val accentLink = if (isDark) Color(0xFF80CBC4) else LxPrimary // teal.opacity(0.9) for dark

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // ---- Header ----
            item {
                HomeHeader(
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    backgroundColor = backgroundColor,
                    isDark = isDark,
                    onProfileClick = onViewAllClick
                )
            }

            // ---- Currently Reading ----
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(18.dp))

                    SectionHeader(
                        title = "Currently Reading",
                        showSeeAll = true,
                        primaryText = primaryText,
                        accentLink = accentLink,
                        onSeeAllClick = onViewAllClick
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.currentlyReadingBook != null) {
                        CurrentlyReadingCard(
                            book = uiState.currentlyReadingBook!!,
                            isDark = isDark,
                            primaryText = primaryText,
                            secondaryText = secondaryText,
                            surfaceColor = surfaceColor,
                            onCoverClick = { onBookClick(uiState.currentlyReadingBook!!) },
                            onUpdateProgress = { onUpdateProgress(uiState.currentlyReadingBook!!) }
                        )
                    } else {
                        EmptyReadingState(
                            primaryText = primaryText,
                            secondaryText = secondaryText,
                            surfaceColor = surfaceColor
                        )
                    }
                }
            }

            // ---- Bento Section (Goal + Stats) ----
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(22.dp))

                    BentoSection(
                        uiState = uiState,
                        isDark = isDark,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        surfaceColor = surfaceColor,
                        onGoalCardClick = { showBooksReadSheet = true }
                    )
                }
            }

        // ---- Freshly Shelved ----
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(22.dp))

                SectionHeader(
                    title = "Freshly Shelved",
                    showSeeAll = false,
                    primaryText = primaryText,
                    accentLink = accentLink,
                    onSeeAllClick = {}
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.recentBooks.isEmpty()) {
                    Text(
                        text = "No books yet. Add books from the + button.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                } else {
                    FreshlyShelvedRow(
                        books = uiState.recentBooks,
                        isDark = isDark,
                        primaryText = primaryText,
                        onBookClick = onBookClick
                    )
                }
            }
        }

        // ---- Latest Notes ----
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(22.dp))

                SectionHeader(
                    title = "Latest Notes",
                    showSeeAll = uiState.latestNotes.isNotEmpty(),
                    primaryText = primaryText,
                    accentLink = accentLink,
                    onSeeAllClick = onViewAllClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                LatestNotesCard(
                    notes = uiState.latestNotes.take(3),
                    isDark = isDark,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    accentLink = accentLink,
                    allBooks = uiState.allBooks,
                    onBookClick = onBookClick
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        }

        // Books Read This Year sheet overlay
        if (showBooksReadSheet) {
            BooksReadThisYearSheet(
                uiState = uiState,
                isDark = isDark,
                backgroundColor = backgroundColor,
                primaryText = primaryText,
                secondaryText = secondaryText,
                surfaceColor = surfaceColor,
                onDismiss = { showBooksReadSheet = false },
                onEditGoal = {
                    showBooksReadSheet = false
                    showGoalEditSheet = true
                }
            )
        }

        // Yearly Goal Edit sheet overlay
        if (showGoalEditSheet) {
            YearlyGoalEditSheet(
                currentGoal = uiState.yearlyGoal,
                isDark = isDark,
                backgroundColor = backgroundColor,
                primaryText = primaryText,
                secondaryText = secondaryText,
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                onSave = { goal ->
                    viewModel.setYearlyGoal(goal)
                    showGoalEditSheet = false
                },
                onDismiss = { showGoalEditSheet = false }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------

@Composable
private fun HomeHeader(
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    borderColor: Color,
    backgroundColor: Color,
    isDark: Boolean,
    onProfileClick: () -> Unit
) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good morning,"
        in 12..16 -> "Good afternoon,"
        else -> "Good evening,"
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor.copy(alpha = 0.96f))
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = greeting,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryText,
                    letterSpacing = 0.4.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reader",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
            }

            // Profile avatar circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = if (isDark) 0.25f else 0.10f),
                        spotColor = Color.Black.copy(alpha = if (isDark) 0.25f else 0.10f)
                    )
                    .clip(CircleShape)
                    .background(if (isDark) LxSurfaceDark else Color.White)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp),
                    tint = secondaryText
                )
            }
        }

        // Bottom border line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    if (isDark) Color(0xFF1C4E5E).copy(alpha = 0.25f)
                    else borderColor.copy(alpha = 0.60f)
                )
        )
    }
}

// ---------------------------------------------------------------------------
// Section Header
// ---------------------------------------------------------------------------

@Composable
private fun SectionHeader(
    title: String,
    showSeeAll: Boolean,
    primaryText: Color,
    accentLink: Color,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryText
        )

        if (showSeeAll) {
            Text(
                text = "View All",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentLink,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Currently Reading Card
// ---------------------------------------------------------------------------

@Composable
private fun CurrentlyReadingCard(
    book: SavedBook,
    isDark: Boolean,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    onCoverClick: () -> Unit,
    onUpdateProgress: () -> Unit
) {
    val progress = when {
        book.pageCount != null && book.pageCount > 0 -> {
            val current = (book.currentPage ?: 0).coerceIn(0, book.pageCount)
            current.toFloat() / book.pageCount.toFloat()
        }
        book.progressFraction != null -> book.progressFraction.toFloat().coerceIn(0f, 1f)
        else -> 0f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCoverClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 0.dp else 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Book cover - fixed size
            if (book.coverURLString != null) {
                AsyncImage(
                    model = book.coverURLString,
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(152.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color.Black.copy(alpha = if (isDark) 0.25f else 0.10f)
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(152.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isDark) Color.White.copy(alpha = 0.10f)
                            else Color.Black.copy(alpha = 0.08f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isDark) Color.White.copy(alpha = 0.35f)
                        else Color.Black.copy(alpha = 0.25f)
                    )
                }
            }

            // Right column - matches cover height exactly
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(152.dp)
            ) {
                // Top: title + author - takes remaining space, pushes bottom down
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.author,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Bottom: progress + button - fixed size, always fully visible
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progress",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = LxAccentGold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isDark) Color.White.copy(alpha = 0.10f)
                                else Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(LxAccentGold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LxPrimary)
                            .clickable { onUpdateProgress() }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Update Progress",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Progress Row
// ---------------------------------------------------------------------------

@Composable
private fun ProgressRow(
    progress: Float,
    isDark: Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray.copy(alpha = 0.8f)
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = LxAccentGold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    if (isDark) Color.White.copy(alpha = 0.10f)
                    else Color.Black.copy(alpha = 0.05f)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(LxAccentGold)
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Empty Reading State
// ---------------------------------------------------------------------------

@Composable
private fun EmptyReadingState(
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "No current book",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Add a book to your Library to start tracking progress.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryText
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Bento Section (Goal Card + 2 Stat Cards)
// ---------------------------------------------------------------------------

@Composable
private fun BentoSection(
    uiState: HomeUiState,
    isDark: Boolean,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    onGoalCardClick: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val goalProgress = if (uiState.yearlyGoal == 0) 0f
    else (uiState.finishedBooksThisYear.toFloat() / uiState.yearlyGoal.toFloat()).coerceAtMost(1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Goal Card (left, tall) - tap to see books read this year
        GoalCard(
            year = currentYear,
            current = uiState.finishedBooksThisYear,
            goal = uiState.yearlyGoal,
            progress = goalProgress,
            isDark = isDark,
            primaryText = primaryText,
            secondaryText = secondaryText,
            surfaceColor = surfaceColor,
            onClick = onGoalCardClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        // Right column: two stat cards
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.LocalFireDepartment,
                iconTint = Color(0xFFFF9800), // orange
                iconBackground = if (isDark) Color(0xFFFF9800).copy(alpha = 0.12f)
                else Color(0xFFFF9800).copy(alpha = 0.10f),
                value = "${uiState.dayStreak}",
                label = "DAY STREAK",
                isDark = isDark,
                primaryText = primaryText,
                secondaryText = secondaryText,
                surfaceColor = surfaceColor,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            StatCard(
                icon = Icons.Default.Book,
                iconTint = Color(0xFF2196F3), // blue
                iconBackground = if (isDark) Color(0xFF2196F3).copy(alpha = 0.14f)
                else Color(0xFF2196F3).copy(alpha = 0.10f),
                value = "${uiState.pagesThisWeek}",
                label = "PAGES WEEK",
                isDark = isDark,
                primaryText = primaryText,
                secondaryText = secondaryText,
                surfaceColor = surfaceColor,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Goal Card
// ---------------------------------------------------------------------------

@Composable
private fun GoalCard(
    year: Int,
    current: Int,
    goal: Int,
    progress: Float,
    isDark: Boolean,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 0.dp else 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row: flag icon + year badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isDark) LxPrimary.copy(alpha = 0.20f)
                            else Color(0xFF009688).copy(alpha = 0.10f) // teal
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isDark) Color(0xFF80CBC4) else LxPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isDark) Color.White.copy(alpha = 0.10f)
                            else Color.Black.copy(alpha = 0.05f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$year",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White.copy(alpha = 0.7f)
                        else Color.Black.copy(alpha = 0.55f)
                    )
                }
            }

            // Bottom: count + label + progress bar
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "$current",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryText
                    )
                    Text(
                        text = "/ $goal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Books Read",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isDark) Color.White.copy(alpha = 0.10f)
                            else Color.Black.copy(alpha = 0.05f)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(LxAccentGold)
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Stat Card
// ---------------------------------------------------------------------------

@Composable
private fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    value: String,
    label: String,
    isDark: Boolean,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 0.dp else 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconTint
                )
            }

            Column {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = secondaryText,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Freshly Shelved (horizontal scroll)
// ---------------------------------------------------------------------------

@Composable
private fun FreshlyShelvedRow(
    books: List<SavedBook>,
    isDark: Boolean,
    primaryText: Color,
    onBookClick: (SavedBook) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        books.forEach { book ->
            Column(
                modifier = Modifier
                    .width(112.dp)
                    .clickable { onBookClick(book) }
            ) {
                if (book.coverURLString != null) {
                    AsyncImage(
                        model = book.coverURLString,
                        contentDescription = book.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isDark) Color.White.copy(alpha = 0.10f)
                                else Color.Black.copy(alpha = 0.08f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isDark) Color.White.copy(alpha = 0.35f)
                            else Color.Black.copy(alpha = 0.25f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = book.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Latest Notes Card
// ---------------------------------------------------------------------------

@Composable
private fun LatestNotesCard(
    notes: List<NoteEntry>,
    isDark: Boolean,
    primaryText: Color,
    secondaryText: Color,
    accentLink: Color,
    allBooks: List<SavedBook>,
    onBookClick: (SavedBook) -> Unit
) {
    val bgColor = LxPrimary.copy(alpha = if (isDark) 0.20f else 0.10f)
    val borderStroke = LxPrimary.copy(alpha = if (isDark) 0.30f else 0.20f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            if (notes.isEmpty()) {
                Text(
                    text = "No notes yet",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Add notes from any book's detail page and they'll show up here.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryText
                )
            } else {
                notes.forEachIndexed { index, entry ->
                    Column(
                        modifier = Modifier.clickable {
                            val book = allBooks.firstOrNull {
                                it.title == entry.bookTitle && it.author == entry.bookAuthor
                            }
                            book?.let { onBookClick(it) }
                        }
                    ) {
                        // Note text (italic, serif-like)
                        Text(
                            text = entry.text,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            color = if (isDark) Color.White.copy(alpha = 0.88f)
                            else Color.Black.copy(alpha = 0.70f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Book title + date
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = entry.bookTitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentLink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = "\u2022", // bullet
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = secondaryText.copy(alpha = 0.7f)
                            )
                            Text(
                                text = formatDate(entry.createdAt),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = secondaryText
                            )
                        }
                    }

                    if (index < notes.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (isDark) Color.White.copy(alpha = 0.08f)
                            else Color.Black.copy(alpha = 0.08f)
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Books Read This Year Sheet
// ---------------------------------------------------------------------------

@Composable
private fun BooksReadThisYearSheet(
    uiState: HomeUiState,
    isDark: Boolean,
    backgroundColor: Color,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    onDismiss: () -> Unit,
    onEditGoal: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val finishedBooks = uiState.allBooks.filter { it.status == BookStatus.FINISHED }
        .sortedByDescending { it.finishedDate ?: 0L }
    val goalProgress = if (uiState.yearlyGoal == 0) 0f
    else (uiState.finishedBooksThisYear.toFloat() / uiState.yearlyGoal.toFloat()).coerceAtMost(1f)

    val dateFormat = remember { SimpleDateFormat("MM/dd/yy", Locale.getDefault()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(top = 14.dp, bottom = 30.dp)
        ) {
            // Header with close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Books Read",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color.White.copy(alpha = 0.10f)
                            else Color.Black.copy(alpha = 0.06f)
                        )
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = secondaryText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${uiState.finishedBooksThisYear}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = LxPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "of ${uiState.yearlyGoal} books read in $currentYear",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryText
                )

                if (uiState.yearlyGoal > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isDark) Color.White.copy(alpha = 0.10f)
                                else Color.Black.copy(alpha = 0.05f)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = goalProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(LxAccentGold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Edit Goal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LxPrimary,
                    modifier = Modifier.clickable { onEditGoal() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Books list
            if (finishedBooks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No books finished yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Books you finish this year will appear here.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText
                    )
                }
            } else {
                finishedBooks.forEach { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isDark) 0.dp else 6.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Cover
                            if (book.coverURLString != null) {
                                AsyncImage(
                                    model = book.coverURLString,
                                    contentDescription = book.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(75.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(75.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isDark) Color.White.copy(alpha = 0.10f)
                                            else Color.Black.copy(alpha = 0.08f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = secondaryText
                                    )
                                }
                            }

                            // Book info
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = book.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = book.author,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (book.finishedDate != null) {
                                    Text(
                                        text = "Finished ${dateFormat.format(Date(book.finishedDate))}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = secondaryText.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            // Checkmark
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Finished",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Yearly Goal Edit Sheet
// ---------------------------------------------------------------------------

@Composable
private fun YearlyGoalEditSheet(
    currentGoal: Int,
    isDark: Boolean,
    backgroundColor: Color,
    primaryText: Color,
    secondaryText: Color,
    surfaceColor: Color,
    borderColor: Color,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var goalText by remember { mutableStateOf(currentGoal.toString()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 14.dp, bottom = 18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Year's Reading Goal ($currentYear)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Text(
                    text = "Save",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LxPrimary,
                    modifier = Modifier.clickable {
                        val goal = goalText.toIntOrNull() ?: currentGoal
                        onSave(goal.coerceIn(1, 999))
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Goal input
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "GOAL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = secondaryText
                )
                TextField(
                    value = goalText,
                    onValueChange = { newValue ->
                        goalText = newValue.filter { it.isDigit() }
                    },
                    placeholder = {
                        Text(
                            "30",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = secondaryText
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(14.dp)),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryText
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = surfaceColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Back button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isDark) Color.White.copy(alpha = 0.08f)
                        else Color.Black.copy(alpha = 0.05f)
                    )
                    .clickable { onDismiss() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = secondaryText
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
