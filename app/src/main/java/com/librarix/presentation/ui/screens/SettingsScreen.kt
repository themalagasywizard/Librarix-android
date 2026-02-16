package com.librarix.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LxAccentGold
import com.librarix.presentation.ui.theme.LxBackgroundDark
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceDark
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

private const val PRIVACY_POLICY_URL = "https://tourmaline-snickerdoodle-c7b749.netlify.app"
private const val SUPPORT_EMAIL = "ryantrumble1997@gmail.com"

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val background = if (isDark) LxBackgroundDark else LxBackgroundLight
    val surface = if (isDark) LxSurfaceDark else LxSurfaceLight
    val border = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.05f)
    val primaryText = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
    val secondaryText = if (isDark) Color.White.copy(alpha = 0.45f) else Color.Black.copy(alpha = 0.45f)

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Local state
    var darkModeEnabled by remember { mutableStateOf(false) }
    var isSignedIn by remember { mutableStateOf(false) }
    var isDeletingAccount by remember { mutableStateOf(false) }

    // Placeholder profile data
    val userName = "Reader"
    val userEmail = "reader@librarix.app"
    val bookCount = 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Top bar
        SettingsTopBar(
            background = background,
            primaryText = primaryText,
            border = border
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(top = 14.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Profile Card
            ProfileCard(
                userName = userName,
                userEmail = userEmail,
                bookCount = bookCount,
                isDark = isDark,
                surface = surface,
                background = background,
                border = border,
                primaryText = primaryText,
                secondaryText = secondaryText,
                onEditProfile = { }
            )

            // Account Section
            SettingsGroup(
                title = "Account",
                isDark = isDark,
                surface = surface,
                border = border
            ) {
                if (!isSignedIn) {
                    // Sign In with Google row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSignedIn = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Google icon placeholder
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "G",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4285F4)
                            )
                        }

                        Text(
                            text = "Sign in with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f),
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = LxTextSecondary.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    // Signed in status
                    SettingsRow(
                        iconBg = if (isDark) Color.Green.copy(alpha = 0.2f) else Color.Green.copy(alpha = 0.1f),
                        icon = Icons.Default.Shield,
                        iconTint = Color.Green,
                        title = "Signed in with Google",
                        primaryText = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f),
                        trailing = { },
                        onClick = { }
                    )

                    HorizontalDivider(
                        color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.12f)
                    )

                    // Sign Out
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSignedIn = false }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Red.copy(alpha = if (isDark) 0.2f else 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Sign Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(
                        color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.12f)
                    )

                    // Delete Account
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isDeletingAccount) { }
                            .padding(16.dp)
                            .then(
                                if (isDeletingAccount) Modifier.background(Color.Transparent)
                                else Modifier
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Red.copy(alpha = if (isDark) 0.25f else 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Delete Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red,
                            modifier = Modifier.weight(1f)
                        )

                        if (isDeletingAccount) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Red,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            // General Section
            SettingsGroup(
                title = "General",
                isDark = isDark,
                surface = surface,
                border = border
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Color(0xFF6366F1).copy(alpha = if (isDark) 0.20f else 0.12f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = if (isDark) Color(0xFF6366F1).copy(alpha = 0.8f) else Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Appearance",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f)
                        )
                        Text(
                            text = if (darkModeEnabled) "Dark" else "Light",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color.White.copy(alpha = 0.45f) else Color.Black.copy(alpha = 0.45f)
                        )
                    }

                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = LxPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray.copy(alpha = 0.35f),
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            // Support Section
            SettingsGroup(
                title = "Support",
                isDark = isDark,
                surface = surface,
                border = border
            ) {
                // Contact Support
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$SUPPORT_EMAIL?subject=Librarix%20Support")
                            }
                            context.startActivity(intent)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // No icon box for support rows (matching iOS)
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Contact Support",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = LxTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                HorizontalDivider(
                    color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.12f)
                )

                // Privacy Policy
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
                            context.startActivity(intent)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Privacy Policy",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White else Color.Black.copy(alpha = 0.92f),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = LxTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Version 1.0.0 (Build 1)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) Color.White.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.35f)
                )

                Text(
                    text = "As an Amazon Associate I earn from qualifying purchases.",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) Color.White.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.25f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -- Top Bar --

@Composable
private fun SettingsTopBar(
    background: Color,
    primaryText: Color,
    border: Color
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(background)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom border line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(border)
        )
    }
}

// -- Profile Card --

@Composable
private fun ProfileCard(
    userName: String,
    userEmail: String,
    bookCount: Int,
    isDark: Boolean,
    surface: Color,
    background: Color,
    border: Color,
    primaryText: Color,
    secondaryText: Color,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.05f)
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Decorative blur circle
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .offset(x = 110.dp, y = (-120).dp)
                    .align(Alignment.TopEnd)
                    .background(
                        LxPrimary.copy(alpha = 0.10f),
                        CircleShape
                    )
                    .blur(26.dp)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(LxPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Name
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Email
                Text(
                    text = userEmail,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stat pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatPill(
                        icon = Icons.Default.Book,
                        tint = LxPrimary,
                        text = "$bookCount Read",
                        isDark = isDark,
                        background = background
                    )
                    StatPill(
                        icon = Icons.Default.Star,
                        tint = LxAccentGold,
                        text = "Top 5%",
                        isDark = isDark,
                        background = background
                    )
                }
            }

            // Edit button (top-right)
            IconButton(
                onClick = onEditProfile,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDark) Color.White.copy(alpha = 0.10f)
                        else Color.Black.copy(alpha = 0.06f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = LxTextSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
private fun StatPill(
    icon: ImageVector,
    tint: Color,
    text: String,
    isDark: Boolean,
    background: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDark) background.copy(alpha = 0.35f) else background)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) Color.White.copy(alpha = 0.75f) else Color.Black.copy(alpha = 0.62f)
        )
    }
}

// -- Settings Group (section card with header) --

@Composable
private fun SettingsGroup(
    title: String,
    isDark: Boolean,
    surface: Color,
    border: Color,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Section header
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
            color = if (isDark) Color.White.copy(alpha = 0.45f) else Color.Black.copy(alpha = 0.45f),
            modifier = Modifier.padding(horizontal = 2.dp)
        )

        // Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surface),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.05f)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

// -- Settings Row (with icon box) --

@Composable
private fun SettingsRow(
    iconBg: Color,
    icon: ImageVector,
    iconTint: Color,
    title: String,
    primaryText: Color,
    trailing: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = primaryText,
            modifier = Modifier.weight(1f)
        )

        trailing()
    }
}
