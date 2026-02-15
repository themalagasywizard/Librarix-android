package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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

@Composable
fun BookDetailScreen(
    book: SavedBook,
    onBackClick: () -> Unit,
    onUpdateProgress: () -> Unit,
    onAddToCollection: () -> Unit,
    onEditBook: () -> Unit,
    onDeleteBook: () -> Unit,
    onShare: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero section with blurred backdrop
            HeroSection(book = book)

            // Title and meta
            TitleSection(book = book)

            Spacer(modifier = Modifier.height(14.dp))

            // Actions bar
            ActionsBar(
                book = book,
                onUpdateProgress = onUpdateProgress,
                onAddToCollection = onAddToCollection,
                onShare = onShare
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Synopsis
            SynopsisSection(
                synopsis = book.description ?: "No synopsis available."
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Notes section placeholder
            NotesSection(
                notesCount = book.notes?.size ?: 0,
                onViewAllNotes = { }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Top navigation
        TopNavBar(
            onBackClick = onBackClick,
            isFavorite = book.isFavorite,
            onToggleFavorite = { },
            showMenu = showMenu,
            onShowMenu = { showMenu = it },
            onEditBook = {
                showMenu = false
                onEditBook()
            },
            onDeleteBook = {
                showMenu = false
                onDeleteBook()
            }
        )
    }
}

@Composable
private fun HeroSection(book: SavedBook) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        // Blurred backdrop
        AsyncImage(
            model = book.coverURLString,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .alpha(0.3f)
                .blur(radius = 30.dp)
                .scale(1.15f),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            LxBackgroundLight.copy(alpha = 0.55f),
                            LxBackgroundLight
                        )
                    )
                )
        )

        // Cover image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 97.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = book.coverURLString,
                contentDescription = book.title,
                modifier = Modifier
                    .width(192.dp)
                    .height(288.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun TitleSection(book: SavedBook) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
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

            Text(
                text = "•",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                text = "${book.pageCount ?: "—"} pages",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Status badge
        Text(
            text = book.status.name.lowercase().replaceFirstChar { it.uppercase() },
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = when (book.status) {
                        BookStatus.FINISHED -> Color.Green
                        BookStatus.READING -> LxPrimary
                        BookStatus.WANT_TO_READ -> Color.Red
                    },
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ActionsBar(
    book: SavedBook,
    onUpdateProgress: () -> Unit,
    onAddToCollection: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Update Progress button
        Button(
            onClick = onUpdateProgress,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Update Progress",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // Add to collection
        IconButton(
            onClick = onAddToCollection,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(LxBorderLight)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Add to collection",
                tint = Color.Black
            )
        }

        // Share
        IconButton(
            onClick = onShare,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(LxBorderLight)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun SynopsisSection(synopsis: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Synopsis",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = synopsis,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun NotesSection(
    notesCount: Int,
    onViewAllNotes: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notes",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Text(
                text = "$notesCount notes",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )
        }
    }
}

@Composable
private fun TopNavBar(
    onBackClick: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    showMenu: Boolean,
    onShowMenu: (Boolean) -> Unit,
    onEditBook: () -> Unit,
    onDeleteBook: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) LxAccentGold else Color.Black
            )
        }

        Box {
            IconButton(onClick = { onShowMenu(true) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Black
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