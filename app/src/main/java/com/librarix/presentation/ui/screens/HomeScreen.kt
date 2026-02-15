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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBadgePlus
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

@Composable
fun HomeScreen(
    onViewAllClick: () -> Unit,
    onBookClick: (SavedBook) -> Unit,
    onUpdateProgress: (SavedBook) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Good evening,",
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary
            )
            Text(
                text = "Reader",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Currently Reading Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Currently Reading",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "View All",
                    fontWeight = FontWeight.SemiBold,
                    color = LxPrimary,
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Currently Reading Card (placeholder)
            CurrentlyReadingCard(
                book = null, // TODO: Get from ViewModel
                onCoverClick = { book -> book?.let { onBookClick(it) } },
                onUpdateProgress = { book -> book?.let { onUpdateProgress(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Goal Card placeholder
            GoalCard(
                current = 0,
                goal = 12,
                progress = 0.0f
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Freshly Shelved placeholder
            Text(
                text = "Freshly Shelved",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No books yet. Add books from the + button.",
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary
            )
        }
    }
}

@Composable
fun CurrentlyReadingCard(
    book: SavedBook?,
    onCoverClick: (SavedBook?) -> Unit,
    onUpdateProgress: (SavedBook?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cover placeholder
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LxBorderLight)
                    .clickable { onCoverClick(book) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book?.title ?: "No current book",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book?.author ?: "Add a book to start tracking",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = LxTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${((book?.progressFraction ?: 0.0) * 100).toInt()}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LxAccentGold
                    )
                }

                LinearProgressIndicator(
                    progress = { (book?.progressFraction ?: 0f).toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = LxAccentGold,
                    trackColor = LxBorderLight
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onUpdateProgress(book) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBadgePlus,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Update Progress",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(
    current: Int,
    goal: Int,
    progress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "2026 Reading Goal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "$current / $goal books",
                    fontWeight = FontWeight.Medium,
                    color = LxTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LxAccentGold,
                trackColor = LxBorderLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$current books read",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = LxTextSecondary
            )
        }
    }
}

// Extension for fontSize in Sp
private val Int.sp: androidx.compose.ui.unit.TextUnit
    get() = androidx.compose.ui.unit.dp * this