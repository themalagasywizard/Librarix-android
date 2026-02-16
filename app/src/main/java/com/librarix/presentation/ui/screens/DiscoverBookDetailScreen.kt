package com.librarix.presentation.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderDark
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

data class DiscoverBookDetail(
    val key: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val synopsis: String,
    val pageCount: Int?,
    val subjects: List<String>?,
    val isbns: List<String>? = null
)

@Composable
fun DiscoverBookDetailScreen(
    book: DiscoverBookDetail,
    isSaved: Boolean = false,
    onBackClick: () -> Unit,
    onSaveToLibrary: () -> Unit,
    onShare: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val backgroundColor = if (isDark) LxBackgroundDark else LxBackgroundLight
    val surfaceColor = if (isDark) LxSurfaceDark else LxSurfaceLight
    val borderColor = if (isDark) LxBorderDark else LxBorderLight
    val textColor = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    var synopsisExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero section
            HeroSection(
                coverUrl = book.coverUrl,
                isDark = isDark,
                backgroundColor = backgroundColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Title and Meta
            TitleAndMeta(
                title = book.title,
                author = book.author,
                firstPublishYear = book.firstPublishYear,
                pageCount = book.pageCount,
                textColor = textColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main content stack
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Actions row
                ActionsRow(
                    isSaved = isSaved,
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor,
                    onSaveToLibrary = onSaveToLibrary,
                    onShare = onShare
                )

                // Synopsis card
                SynopsisCard(
                    synopsis = book.synopsis,
                    synopsisExpanded = synopsisExpanded,
                    onToggle = { synopsisExpanded = !synopsisExpanded },
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor
                )

                // Author mini card
                AuthorMiniCard(
                    author = book.author,
                    isDark = isDark,
                    textColor = textColor,
                    surfaceColor = surfaceColor,
                    borderColor = borderColor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Sticky top navigation
        TopNavBar(
            onBackClick = onBackClick,
            onMoreClick = onShare,
            isDark = isDark,
            backgroundColor = backgroundColor
        )
    }
}

// ─── Top Navigation ──────────────────────────────────────────────────────────────

@Composable
private fun TopNavBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    isDark: Boolean,
    backgroundColor: Color
) {
    val iconColor = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    val dividerColor = if (isDark) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.06f)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(dividerColor)
        )
    }
}

// ─── Hero Section ────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(
    coverUrl: String?,
    isDark: Boolean,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
    ) {
        // Blurred background
        AsyncImage(
            model = coverUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .graphicsLayer {
                    scaleX = 1.15f
                    scaleY = 1.15f
                    alpha = if (isDark) 0.18f else 0.30f
                }
                .blur(30.dp),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
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

        // Cover image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 97.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(192.dp)
                    .height(288.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color.Black.copy(alpha = 0.50f),
                        spotColor = Color.Black.copy(alpha = 0.50f)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// ─── Title and Meta ──────────────────────────────────────────────────────────────

@Composable
private fun TitleAndMeta(
    title: String,
    author: String,
    firstPublishYear: Int?,
    pageCount: Int?,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = author,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxPrimary
            )

            // Bullet
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(LxTextSecondary.copy(alpha = 0.7f), CircleShape)
            )

            Text(
                text = firstPublishYear?.toString() ?: "—",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )

            // Bullet
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(LxTextSecondary.copy(alpha = 0.7f), CircleShape)
            )

            Text(
                text = if (pageCount != null && pageCount > 0) "$pageCount Pages" else "Pages —",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )
        }
    }
}

// ─── Actions Row ─────────────────────────────────────────────────────────────────

@Composable
private fun ActionsRow(
    isSaved: Boolean,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    onSaveToLibrary: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Add to Library / Saved button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color.Black.copy(alpha = 0.20f),
                    spotColor = Color.Black.copy(alpha = 0.20f)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (isSaved) Color.Green.copy(alpha = 0.9f)
                    else if (isDark) Color.White.copy(alpha = 0.92f)
                    else Color.Black.copy(alpha = 0.92f)
                )
                .clickable { if (!isSaved) onSaveToLibrary() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSaved) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Saved",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "+",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = if (isDark) Color.Black.copy(alpha = 0.92f) else Color.White
                    )
                    Text(
                        text = "Add to Library",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isDark) Color.Black.copy(alpha = 0.92f) else Color.White
                    )
                }
            }
        }

        // Cart button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Buy",
                tint = com.librarix.presentation.ui.theme.LxAccentGold,
                modifier = Modifier.size(18.dp)
            )
        }

        // Share button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                .clickable { onShare() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Share",
                tint = if (isDark) Color.White.copy(alpha = 0.75f) else Color.Black.copy(alpha = 0.62f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Synopsis Card ───────────────────────────────────────────────────────────────

@Composable
private fun SynopsisCard(
    synopsis: String,
    synopsisExpanded: Boolean,
    onToggle: () -> Unit,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color
) {
    val synopsisText = synopsis.ifBlank { "No synopsis available yet." }
    val textColor = if (isDark) Color.White.copy(alpha = 0.82f) else Color.Black.copy(alpha = 0.70f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
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
            text = synopsisText,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = textColor,
            maxLines = if (synopsisExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { onToggle() }
        )

        // Expand/collapse chevron
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (synopsisExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = if (synopsisExpanded) "Collapse" else "Expand",
                tint = LxPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ─── Author Mini Card ────────────────────────────────────────────────────────────

@Composable
private fun AuthorMiniCard(
    author: String,
    isDark: Boolean,
    textColor: Color,
    surfaceColor: Color,
    borderColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isDark) Color.White.copy(alpha = 0.10f)
                    else Color.Black.copy(alpha = 0.08f)
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

        // Name and subtitle
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = author,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = textColor
            )
            Text(
                text = "Author",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = LxTextSecondary
            )
        }

        // View Profile button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(LxPrimary.copy(alpha = 0.10f))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "View Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = LxPrimary
            )
        }
    }
}
