package com.librarix.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.librarix.domain.model.BookStatus
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.screens.BookDetailScreen
import com.librarix.presentation.ui.screens.CurrentlyReadingListScreen
import com.librarix.presentation.ui.screens.DiscoverScreen
import com.librarix.presentation.ui.screens.HomeScreen
import com.librarix.presentation.ui.screens.LibraryScreen
import com.librarix.presentation.ui.screens.ProfileScreen
import com.librarix.presentation.ui.screens.UpdateProgressView
import com.librarix.presentation.ui.theme.LibrarixTheme
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxTextSecondary
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

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Discover : Screen("discover", "Discover", Icons.Filled.Explore, Icons.Outlined.Explore)
    data object Library : Screen("library", "Library", Icons.AutoMirrored.Filled.MenuBook, Icons.Outlined.MenuBook)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
    data object CurrentlyReadingList : Screen("currently_reading_list", "Reading", Icons.Filled.Home, Icons.Outlined.Home)
    data object BookDetail : Screen("book_detail/{bookId}", "Book", Icons.Filled.Home, Icons.Outlined.Home) {
        fun createRoute(bookId: String) = "book_detail/$bookId"
    }
    data object UpdateProgress : Screen("update_progress/{bookId}", "Progress", Icons.Filled.Home, Icons.Outlined.Home) {
        fun createRoute(bookId: String) = "update_progress/$bookId"
    }
}

val bottomNavItems = listOf(Screen.Home, Screen.Discover, Screen.Library, Screen.Profile)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var currentBook by remember { mutableStateOf<SavedBook?>(null) }
    var showProgressSheet by remember { mutableStateOf(false) }
    var showBookDetail by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Hide bottom nav on detail screens
            val showBottomNav = currentDestination?.route in listOf(
                Screen.Home.route,
                Screen.Discover.route,
                Screen.Library.route,
                Screen.Profile.route
            )

            if (showBottomNav) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onViewAllClick = {
                        navController.navigate(Screen.CurrentlyReadingList.route)
                    },
                    onBookClick = { book ->
                        currentBook = book
                        showBookDetail = true
                    },
                    onUpdateProgress = { book ->
                        currentBook = book
                        showProgressSheet = true
                    }
                )
            }

            composable(Screen.Discover.route) {
                DiscoverScreen(
                    onBookClick = { book ->
                        currentBook = book
                        showBookDetail = true
                    }
                )
            }

            composable(Screen.Library.route) {
                LibraryScreen(
                    books = emptyList(), // TODO: Load from ViewModel
                    onBookClick = { book ->
                        currentBook = book
                        showBookDetail = true
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSettingsClick = { }
                )
            }

            composable(Screen.CurrentlyReadingList.route) {
                CurrentlyReadingListScreen(
                    books = emptyList(), // TODO: Load from ViewModel
                    onBookClick = { book ->
                        currentBook = book
                        showProgressSheet = true
                    },
                    onDismiss = { navController.popBackStack() }
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
                onDeleteBook = { },
                onShare = { }
            )
        }

        // Progress update overlay
        if (showProgressSheet && currentBook != null) {
            UpdateProgressView(
                book = currentBook!!,
                onSave = { updatedBook ->
                    // TODO: Save to repository
                    showProgressSheet = false
                },
                onDismiss = { showProgressSheet = false }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LxPrimary,
                    selectedTextColor = LxPrimary,
                    unselectedIconColor = LxTextSecondary,
                    unselectedTextColor = LxTextSecondary,
                    indicatorColor = LxPrimary.copy(alpha = 0.1f)
                )
            )
        }
    }
}