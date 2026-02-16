package com.librarix.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.librarix.data.remote.OpenLibraryDoc
import com.librarix.data.remote.OpenLibrarySubjectWork
import com.librarix.data.remote.TrendingBookData
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.screens.AddBookScreen
import com.librarix.presentation.ui.screens.BookDetailScreen
import com.librarix.presentation.ui.screens.DiscoverBookDetail
import com.librarix.presentation.ui.screens.DiscoverBookDetailScreen
import com.librarix.presentation.ui.screens.DiscoverScreen
import com.librarix.presentation.ui.screens.HomeScreen
import com.librarix.presentation.ui.screens.LibraryScreen
import com.librarix.presentation.ui.screens.SettingsScreen
import com.librarix.presentation.ui.screens.UpdateProgressView
import com.librarix.presentation.ui.theme.LibrarixTheme
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderDark
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibrarixTheme {
                MainScreen()
            }
        }
    }
}

enum class AppTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    LIBRARY("Library", Icons.AutoMirrored.Filled.MenuBook, Icons.Outlined.MenuBook),
    DISCOVER("Discover", Icons.Filled.Search, Icons.Outlined.Search),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }
    var currentBook by remember { mutableStateOf<SavedBook?>(null) }
    var showProgressSheet by remember { mutableStateOf(false) }
    var showBookDetail by remember { mutableStateOf(false) }
    var showAddBook by remember { mutableStateOf(false) }
    var discoverBookDetail by remember { mutableStateOf<DiscoverBookDetail?>(null) }
    var showDiscoverDetail by remember { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeState by homeViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!showBookDetail && !showProgressSheet && !showAddBook && !showDiscoverDetail) {
                LibrarixBottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onAddClick = { showAddBook = true }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                AppTab.HOME -> HomeScreen(
                    onViewAllClick = { },
                    onBookClick = { book ->
                        currentBook = book
                        showBookDetail = true
                    },
                    onUpdateProgress = { book ->
                        currentBook = book
                        showProgressSheet = true
                    }
                )
                AppTab.LIBRARY -> LibraryScreen(
                    books = homeState.allBooks,
                    onBookClick = { book ->
                        currentBook = book
                        showBookDetail = true
                    }
                )
                AppTab.DISCOVER -> DiscoverScreen(
                    onBookClick = { bookAny ->
                        val detail = when (bookAny) {
                            is TrendingBookData -> DiscoverBookDetail(
                                key = bookAny.isbn,
                                title = bookAny.displayTitle,
                                author = bookAny.displayAuthor,
                                coverUrl = bookAny.coverURL,
                                firstPublishYear = null,
                                synopsis = bookAny.synopsis ?: bookAny.metadata?.description ?: "",
                                pageCount = bookAny.pageCount,
                                subjects = bookAny.metadata?.categories,
                                isbns = listOf(bookAny.isbn)
                            )
                            is OpenLibraryDoc -> DiscoverBookDetail(
                                key = bookAny.key,
                                title = bookAny.displayTitle,
                                author = bookAny.displayAuthor,
                                coverUrl = bookAny.coverUrl("L"),
                                firstPublishYear = bookAny.firstPublishYear,
                                synopsis = "",
                                pageCount = bookAny.numberOfPagesMedian,
                                subjects = null,
                                isbns = bookAny.isbn
                            )
                            is OpenLibrarySubjectWork -> DiscoverBookDetail(
                                key = bookAny.key,
                                title = bookAny.displayTitle,
                                author = bookAny.displayAuthor,
                                coverUrl = bookAny.coverUrl("L"),
                                firstPublishYear = bookAny.firstPublishYear,
                                synopsis = "",
                                pageCount = null,
                                subjects = null,
                                isbns = null
                            )
                            else -> null
                        }
                        if (detail != null) {
                            discoverBookDetail = detail
                            showDiscoverDetail = true
                        }
                    }
                )
                AppTab.SETTINGS -> SettingsScreen(
                    onNavigateBack = { }
                )
            }
        }

        // Book detail overlay
        if (showBookDetail && currentBook != null) {
            BookDetailScreen(
                book = currentBook!!,
                onBackClick = { showBookDetail = false },
                onUpdateProgress = {
                    showBookDetail = false
                    showProgressSheet = true
                },
                onAddToCollection = { },
                onEditBook = { },
                onDeleteBook = {
                    homeViewModel.deleteBook(currentBook!!.id)
                    showBookDetail = false
                },
                onStatusChange = { status ->
                    homeViewModel.updateStatus(currentBook!!.id, status)
                    currentBook = currentBook!!.copy(status = status)
                },
                onRatingChange = { rating ->
                    homeViewModel.updateRating(currentBook!!.id, rating)
                    currentBook = currentBook!!.copy(rating = rating)
                },
                onToggleFavorite = {
                    homeViewModel.toggleFavorite(currentBook!!.id)
                    currentBook = currentBook!!.copy(isFavorite = !currentBook!!.isFavorite)
                },
                onShare = { }
            )
        }

        // Progress update overlay
        if (showProgressSheet && currentBook != null) {
            UpdateProgressView(
                book = currentBook!!,
                onSave = { updatedBook ->
                    homeViewModel.updateBook(updatedBook)
                    currentBook = updatedBook
                    showProgressSheet = false
                },
                onDismiss = { showProgressSheet = false }
            )
        }

        // Add book overlay
        if (showAddBook) {
            AddBookScreen(
                onDismiss = { showAddBook = false },
                onBookAdded = { showAddBook = false }
            )
        }

        // Discover book detail overlay
        if (showDiscoverDetail && discoverBookDetail != null) {
            val detail = discoverBookDetail!!
            val isSaved = homeState.allBooks.any { saved ->
                (detail.isbns?.any { isbn -> isbn == saved.isbn } == true) ||
                    (detail.key == saved.openLibraryWorkKey) ||
                    (detail.title.equals(saved.title, ignoreCase = true) && detail.author.equals(saved.author, ignoreCase = true))
            }
            DiscoverBookDetailScreen(
                book = detail,
                isSaved = isSaved,
                onBackClick = { showDiscoverDetail = false },
                onSaveToLibrary = {
                    if (!isSaved) {
                        val savedBook = SavedBook(
                            id = java.util.UUID.randomUUID().toString(),
                            title = detail.title,
                            author = detail.author,
                            description = detail.synopsis.ifBlank { null },
                            coverURLString = detail.coverUrl,
                            pageCount = detail.pageCount,
                            currentPage = 0,
                            status = BookStatus.WANT_TO_READ,
                            isbn = detail.isbns?.firstOrNull(),
                            openLibraryWorkKey = detail.key,
                            addedDate = System.currentTimeMillis()
                        )
                        homeViewModel.addBook(savedBook)
                    }
                },
                onShare = { }
            )
        }
    }
}

@Composable
fun LibrarixBottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    onAddClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) LxBackgroundDark else Color.White
    val borderColor = if (isDark) LxBorderDark else LxBorderLight
    val inactiveColor = if (isDark) Color.White.copy(alpha = 0.40f) else Color.Black.copy(alpha = 0.35f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
    ) {
        // Top border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
                .align(Alignment.TopCenter)
        )

        // Nav bar background and items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(backgroundColor)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            NavBarItem(
                tab = AppTab.HOME,
                isSelected = selectedTab == AppTab.HOME,
                activeColor = LxPrimary,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(AppTab.HOME) },
                modifier = Modifier.weight(1f)
            )
            // Library
            NavBarItem(
                tab = AppTab.LIBRARY,
                isSelected = selectedTab == AppTab.LIBRARY,
                activeColor = LxPrimary,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(AppTab.LIBRARY) },
                modifier = Modifier.weight(1f)
            )
            // Discover
            NavBarItem(
                tab = AppTab.DISCOVER,
                isSelected = selectedTab == AppTab.DISCOVER,
                activeColor = LxPrimary,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(AppTab.DISCOVER) },
                modifier = Modifier.weight(1f)
            )
            // Settings
            NavBarItem(
                tab = AppTab.SETTINGS,
                isSelected = selectedTab == AppTab.SETTINGS,
                activeColor = LxPrimary,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(AppTab.SETTINGS) },
                modifier = Modifier.weight(1f)
            )
        }

        // Floating "+" button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = if (isDark) 0.40f else 0.18f),
                    spotColor = Color.Black.copy(alpha = if (isDark) 0.40f else 0.18f)
                )
                .size(56.dp)
                .clip(CircleShape)
                .background(LxPrimary)
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Book",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun NavBarItem(
    tab: AppTab,
    isSelected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) activeColor else inactiveColor
    val icon = if (isSelected) tab.selectedIcon else tab.unselectedIcon

    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = tab.title,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = tab.title,
            color = color,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
