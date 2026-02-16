package com.librarix.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.librarix.data.remote.AmazonAffiliateLinkGenerator
import com.librarix.data.remote.CollectionBookItem
import com.librarix.data.remote.CuratedCollection
import com.librarix.data.remote.OpenLibraryDoc
import com.librarix.data.remote.OpenLibrarySubjectWork
import com.librarix.data.remote.TrendingBookData
import com.librarix.domain.model.DiscoverBook
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary
import com.librarix.presentation.viewmodel.DiscoverChip
import com.librarix.presentation.viewmodel.DiscoverViewModel

// ─── Main Screen ────────────────────────────────────────────────────────────────

@Composable
fun DiscoverScreen(
    onBookClick: (Any) -> Unit
) {
    val viewModel: DiscoverViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        // Sticky header
        DiscoverHeader(
            searchQuery = state.searchQuery,
            onSearchChanged = viewModel::onSearchChanged
        )

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Chips
            item {
                Spacer(modifier = Modifier.height(14.dp))
                DiscoverChipsRow(
                    selectedChip = state.selectedChip,
                    onChipChanged = viewModel::onChipChanged
                )
            }

            // Error message
            state.errorMessage?.let { msg ->
                item {
                    Text(
                        text = msg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Red.copy(alpha = 0.85f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }

            // Content routing
            val searchActive = state.searchQuery.trim().isNotEmpty()
            if (searchActive) {
                // Search results
                item {
                    SearchResultsSection(
                        results = state.searchResults,
                        isSearching = state.isSearching,
                        onBookClick = onBookClick
                    )
                }
            } else if (state.selectedChip == DiscoverChip.MY_COLLECTIONS) {
                item {
                    MyCollectionsPlaceholder()
                }
            } else {
                // For You feed
                item {
                    HeroRecommendationCard(
                        pickOfTheWeek = state.pickOfTheWeek,
                        isLoading = state.isLoading,
                        onViewDetails = { book ->
                            onBookClick(book)
                        }
                    )
                }

                item {
                    TrendingNowSection(
                        trending = state.trending,
                        trendingWorks = state.trendingWorks,
                        isLoading = state.isLoading,
                        onBookClick = onBookClick
                    )
                }

                if (state.collections.isNotEmpty()) {
                    item {
                        CuratedCollectionsSection(
                            collections = state.collections,
                            onCollectionClick = { /* collection detail */ },
                            onBookClick = onBookClick
                        )
                    }
                }

                if (state.topSellers.isNotEmpty()) {
                    item {
                        WeeklyTopSellersSection(
                            topSellers = state.topSellers.take(6),
                            onBookClick = onBookClick
                        )
                    }
                }
            }
        }
    }
}

// ─── Header ─────────────────────────────────────────────────────────────────────

@Composable
private fun DiscoverHeader(
    searchQuery: String,
    onSearchChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LxBackgroundLight)
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 10.dp)
    ) {
        Text(
            text = "Discover",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black.copy(alpha = 0.92f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchChanged,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)),
            placeholder = {
                Text(
                    "Title, author, or ISBN...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = LxTextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = LxTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = LxTextSecondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Black.copy(alpha = 0.06f),
                focusedContainerColor = Color.Black.copy(alpha = 0.06f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black.copy(alpha = 0.90f)
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.90f)
            )
        )
    }

    // Bottom border line (matches iOS: black.opacity(0.06) light mode)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black.copy(alpha = 0.06f))
    )
}

// ─── Chips Row ──────────────────────────────────────────────────────────────────

@Composable
private fun DiscoverChipsRow(
    selectedChip: DiscoverChip,
    onChipChanged: (DiscoverChip) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        DiscoverChip.entries.forEach { chip ->
            val isActive = selectedChip == chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isActive) LxPrimary
                        else Color.Black.copy(alpha = 0.06f)
                    )
                    .clickable { onChipChanged(chip) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = chip.title,
                    fontSize = 14.sp,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isActive) Color.White
                    else Color.Black.copy(alpha = 0.65f)
                )
            }
        }
    }
}

// ─── Hero Recommendation Card ───────────────────────────────────────────────────

@Composable
private fun HeroRecommendationCard(
    pickOfTheWeek: TrendingBookData?,
    isLoading: Boolean,
    onViewDetails: (TrendingBookData) -> Unit
) {
    if (pickOfTheWeek != null) {
        val coverUrl = pickOfTheWeek.coverURL

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(22.dp))
                .shadow(
                    elevation = 22.dp,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = Color.Black.copy(alpha = 0.25f)
                )
        ) {
            // Dark background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LxSurfaceDark)
            )

            // Blurred cover background
            if (coverUrl != null) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { scaleX = 1.25f; scaleY = 1.25f; alpha = 0.30f }
                        .blur(26.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Semi-transparent overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LxSurfaceDark.copy(alpha = 0.96f))
            )

            // Content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cover image with badge
                Box {
                    AsyncImage(
                        model = coverUrl,
                        contentDescription = pickOfTheWeek.displayTitle,
                        modifier = Modifier
                            .width(128.dp)
                            .height(192.dp)
                            .rotate(2f)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(
                                elevation = 18.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color.Black.copy(alpha = 0.40f)
                            ),
                        contentScale = ContentScale.Crop
                    )

                    // Rating badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 10.dp, y = 10.dp)
                            .background(LxAccentGold, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "4.9",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                // Text content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "BOOK OF THE WEEK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LxPrimary,
                            letterSpacing = 1.4.sp
                        )

                        Text(
                            text = pickOfTheWeek.displayTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "by ${pickOfTheWeek.displayAuthor}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LxTextSecondary
                        )

                        // Reason chip
                        val reasonText = pickOfTheWeek.reason?.takeIf { it.isNotEmpty() }
                            ?: "Because you read The Alchemist"
                        Row(
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.05f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = reasonText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.75f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // View Details button
                    Button(
                        onClick = { onViewDetails(pickOfTheWeek) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LxPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(LxSurfaceDark),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

// ─── Trending Now Section ───────────────────────────────────────────────────────

@Composable
private fun TrendingNowSection(
    trending: List<TrendingBookData>,
    trendingWorks: List<OpenLibrarySubjectWork>,
    isLoading: Boolean,
    onBookClick: (Any) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Trending Now",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.92f),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (trending.isEmpty() && isLoading) {
                // Placeholders
                items(3) {
                    TrendingCardPlaceholder()
                }
            } else if (trending.isNotEmpty()) {
                items(trending, key = { it.isbn }) { book ->
                    TrendingCard(
                        title = book.displayTitle,
                        author = book.displayAuthor,
                        coverUrl = book.coverURL,
                        onClick = { onBookClick(book) }
                    )
                }
            } else {
                items(trendingWorks, key = { it.key }) { work ->
                    TrendingCard(
                        title = work.displayTitle,
                        author = work.displayAuthor,
                        coverUrl = work.coverUrl("M"),
                        onClick = { onBookClick(work) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingCard(
    title: String,
    author: String,
    coverUrl: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = coverUrl,
            contentDescription = title,
            modifier = Modifier
                .width(120.dp)
                .height(180.dp) // 120 / (2/3) = 180
                .clip(RoundedCornerShape(12.dp))
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.12f)
                ),
            contentScale = ContentScale.Crop
        )

        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.92f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = author,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = LxTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TrendingCardPlaceholder() {
    Column(
        modifier = Modifier.width(120.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }

        Text(
            text = "Loading...",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.70f),
            maxLines = 1
        )

        Text(
            text = "Trending",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = LxTextSecondary,
            maxLines = 1
        )
    }
}

// ─── Curated Collections Section ────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CuratedCollectionsSection(
    collections: List<CuratedCollection>,
    onCollectionClick: (CuratedCollection) -> Unit,
    onBookClick: (Any) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Curated Collections",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.92f),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // Group into pages of 3
        val unique = collections.distinctBy { it.name }
        val pages = unique.chunked(3)

        if (pages.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { pages.size })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 20.dp),
                pageSpacing = 12.dp
            ) { pageIndex ->
                val page = pages[pageIndex]
                val invert = pageIndex % 2 == 1
                CuratedCollectionPage(
                    featured = page[0],
                    smallTop = page.getOrNull(1),
                    smallBottom = page.getOrNull(2),
                    invert = invert,
                    onCollectionClick = onCollectionClick
                )
            }
        }
    }
}

@Composable
private fun CuratedCollectionPage(
    featured: CuratedCollection,
    smallTop: CuratedCollection?,
    smallBottom: CuratedCollection?,
    invert: Boolean,
    onCollectionClick: (CuratedCollection) -> Unit
) {
    val midnightSlate = Color(0xFF222630)

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val largeTile = @Composable {
            CuratedCollectionTile(
                collection = featured,
                isFeatured = true,
                midnightSlate = midnightSlate,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable { onCollectionClick(featured) }
            )
        }

        val smallColumn = @Composable {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (smallTop != null) {
                    CuratedCollectionTile(
                        collection = smallTop,
                        isFeatured = false,
                        midnightSlate = midnightSlate,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clickable { onCollectionClick(smallTop) }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (smallBottom != null) {
                    CuratedCollectionTile(
                        collection = smallBottom,
                        isFeatured = false,
                        midnightSlate = midnightSlate,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clickable { onCollectionClick(smallBottom) }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (invert) {
            smallColumn()
            largeTile()
        } else {
            largeTile()
            smallColumn()
        }
    }
}

@Composable
private fun CuratedCollectionTile(
    collection: CuratedCollection,
    isFeatured: Boolean,
    midnightSlate: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(midnightSlate)
    ) {
        // Cover fan at top
        val covers = collection.books.take(2)
        val coverWidth = if (isFeatured) 78.dp else 56.dp
        val coverHeight = if (isFeatured) 117.dp else 84.dp
        val coverOverlap = if (isFeatured) (-10).dp else (-8).dp
        val coverOpacity = if (isFeatured) 0.95f else 0.70f

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFeatured) 22.dp else 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            covers.forEachIndexed { i, item ->
                val rotation = if (isFeatured) (i * 4f - 4f) else (i * 3f - 3f)
                val coverUrl = item.coverUrl
                    ?: "https://covers.openlibrary.org/b/isbn/${item.isbn}-M.jpg"
                AsyncImage(
                    model = coverUrl,
                    contentDescription = item.title,
                    modifier = Modifier
                        .width(coverWidth)
                        .height(coverHeight)
                        .then(
                            if (i > 0) Modifier.offset(x = coverOverlap * i)
                            else Modifier
                        )
                        .rotate(rotation)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color.Black.copy(alpha = 0.35f)
                        )
                        .graphicsLayer { alpha = coverOpacity },
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Text at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .then(
                    if (!isFeatured) Modifier.background(
                        Brush.verticalGradient(
                            colors = listOf(
                                midnightSlate.copy(alpha = 0.85f),
                                midnightSlate.copy(alpha = 0.95f)
                            )
                        )
                    ) else Modifier
                )
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Subtitle label
            Text(
                text = if (isFeatured) "FEATURED" else collection.name.uppercase(),
                fontSize = 10.sp,
                fontWeight = if (!isFeatured) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (isFeatured) LxAccentGold else Color.White.copy(alpha = 0.55f),
                letterSpacing = 1.4.sp
            )

            Text(
                text = collection.name,
                fontSize = if (isFeatured) 18.sp else 16.sp,
                fontWeight = if (!isFeatured) FontWeight.ExtraBold else FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (isFeatured) {
                Text(
                    text = "${collection.books.size} books",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }
        }

        // Border overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(18.dp))
                .background(Color.Transparent)
        )
    }
}

// ─── Weekly Top Sellers Section ─────────────────────────────────────────────────

@Composable
private fun WeeklyTopSellersSection(
    topSellers: List<OpenLibraryDoc>,
    onBookClick: (Any) -> Unit
) {
    val context = LocalContext.current
    val amazonLinks = AmazonAffiliateLinkGenerator()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Header with NYT badge
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly Top Sellers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.92f)
            )

            Text(
                text = "NYT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier
                    .background(
                        Color.Red.copy(alpha = 0.18f),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Seller rows
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            topSellers.forEach { doc ->
                TopSellerRow(
                    doc = doc,
                    onClick = { onBookClick(doc) },
                    onBuyClick = {
                        val url = amazonLinks.generateBuyLink(
                            isbn = doc.bestISBN,
                            title = doc.displayTitle,
                            author = doc.displayAuthor
                        )
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Amazon disclaimer
        Text(
            text = "As an Amazon Associate we earn from qualifying purchases.",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun TopSellerRow(
    doc: OpenLibraryDoc,
    onClick: () -> Unit,
    onBuyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LxSurfaceDark)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cover
        AsyncImage(
            model = doc.coverUrl("S"),
            contentDescription = doc.displayTitle,
            modifier = Modifier
                .width(64.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        // Text + Buy
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = doc.displayTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = doc.displayAuthor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onBuyClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LxPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LxPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Buy",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Search Results ─────────────────────────────────────────────────────────────

@Composable
private fun SearchResultsSection(
    results: List<OpenLibraryDoc>,
    isSearching: Boolean,
    onBookClick: (Any) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Results",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.92f)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        // 2-column grid - use a non-lazy grid inside the LazyColumn
        val chunkedResults = results.chunked(2)
        chunkedResults.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowItems.forEach { doc ->
                    SearchResultCard(
                        doc = doc,
                        onClick = { onBookClick(doc) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if odd number
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun SearchResultCard(
    doc: OpenLibraryDoc,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = doc.coverUrl("M"),
            contentDescription = doc.displayTitle,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(12.dp))
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.12f)
                ),
            contentScale = ContentScale.Crop
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = doc.displayTitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.92f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = doc.displayAuthor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = LxTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── My Collections Placeholder ─────────────────────────────────────────────────

@Composable
private fun MyCollectionsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My Collections",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = LxTextSecondary
        )
    }
}
