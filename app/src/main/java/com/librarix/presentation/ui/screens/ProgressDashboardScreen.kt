package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

data class ReadingStats(
    val totalXP: Int = 0,
    val currentLevel: Int = 1,
    val dayStreak: Int = 0,
    val booksFinished: Int = 0,
    val pagesThisWeek: Int = 0,
    val yearlyGoal: Int = 12,
    val yearlyProgress: Float = 0f
)

@Composable
fun ProgressDashboardScreen(
    stats: ReadingStats = ReadingStats(),
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
            .verticalScroll(scrollState)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LxPrimary
                )
            }

            Text(
                text = "My Progress",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Hero level card
        HeroLevelCard(
            totalXP = stats.totalXP,
            currentLevel = stats.currentLevel,
            dayStreak = stats.dayStreak
        )

        Spacer(modifier = Modifier.height(18.dp))

        // High level stats
        HighLevelStats(
            booksFinished = stats.booksFinished,
            pagesThisWeek = stats.pagesThisWeek
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Yearly goal
        YearlyGoalCard(
            current = (stats.yearlyProgress * stats.yearlyGoal).toInt(),
            goal = stats.yearlyGoal
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Achievement badges placeholder
        AchievementBadges()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HeroLevelCard(
    totalXP: Int,
    currentLevel: Int,
    dayStreak: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Level ring
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .shadow(10.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Background gradient glow
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    LxPrimary.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LEVEL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LxTextSecondary
                    )

                    Text(
                        text = "$currentLevel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = LxPrimary
                    )

                    Text(
                        text = "$totalXP XP",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = LxTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Streak badge
            if (dayStreak > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = LxAccentGold.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "$dayStreak day streak",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LxPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun HighLevelStats(
    booksFinished: Int,
    pagesThisWeek: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = Icons.Default.MenuBook,
            iconColor = LxPrimary,
            value = "$booksFinished",
            label = "Books Read",
            modifier = Modifier.weight(1f)
        )

        StatCard(
            icon = Icons.Default.Star,
            iconColor = LxAccentGold,
            value = "$pagesThisWeek",
            label = "Pages Week",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = LxTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun YearlyGoalCard(
    current: Int,
    goal: Int
) {
    val progress = if (goal > 0) current.toFloat() / goal else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2026 Goal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Text(
                    text = "$current / $goal books",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = LxTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = LxAccentGold,
                trackColor = LxBorderLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(progress * 100).toInt()}% complete",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = LxTextSecondary
            )
        }
    }
}

@Composable
fun AchievementBadges() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Achievements",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AchievementBadge(
                icon = Icons.Default.EmojiEvents,
                title = "First Book",
                description = "Finish your first book",
                isUnlocked = false,
                modifier = Modifier.weight(1f)
            )

            AchievementBadge(
                icon = Icons.Default.LocalFireDepartment,
                title = "Week Streak",
                description = "Read for 7 days",
                isUnlocked = false,
                modifier = Modifier.weight(1f)
            )

            AchievementBadge(
                icon = Icons.Default.Star,
                title = "Power Reader",
                description = "Read 1000 pages",
                isUnlocked = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AchievementBadge(
    icon: ImageVector,
    title: String,
    description: String,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) LxAccentGold.copy(alpha = 0.1f) else LxSurfaceLight
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isUnlocked) LxAccentGold else LxBorderLight,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isUnlocked) Color.Black else LxTextSecondary
            )

            Text(
                text = description,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = LxTextSecondary
            )
        }
    }
}