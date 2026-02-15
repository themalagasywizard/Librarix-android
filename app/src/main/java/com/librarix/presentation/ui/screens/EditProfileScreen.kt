package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flame
import androidx.compose.material.icons.filled.Leaf
import androidx.compose.material.icons.filled.Moon
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sparkles
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

data class UserProfile(
    val name: String,
    val email: String = "",
    val avatarSymbol: String = "person.fill",
    val avatarColorHex: Long = 0x1C4E5E
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(
    initialProfile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(initialProfile.name) }
    var avatarSymbol by remember { mutableStateOf(initialProfile.avatarSymbol) }
    var avatarColorHex by remember { mutableStateOf(initialProfile.avatarColorHex) }

    val symbols = listOf(
        "person.fill" to Icons.Default.Person,
        "book.fill" to Icons.Default.Book,
        "sparkles" to Icons.Default.Sparkles,
        "leaf.fill" to Icons.Default.Leaf,
        "flame.fill" to Icons.Default.Flame,
        "moon.stars.fill" to Icons.Default.Moon
    )

    val colors = listOf(
        0x1C4E5E, // Primary teal
        0x3B82F6, // Blue
        0x8B5CF6, // Purple
        0x10B981, // Green
        0xF59E0B, // Amber
        0xEF4444  // Red
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = LxTextSecondary
                )
            }

            Text(
                text = "Edit Profile",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    onSave(
                        UserProfile(
                            name = name,
                            email = initialProfile.email,
                            avatarSymbol = avatarSymbol,
                            avatarColorHex = avatarColorHex
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Save",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LxPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar Preview
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(Color(avatarColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = symbols.find { it.first == avatarSymbol }?.second ?: Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fields & Pickers Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = LxSurfaceLight,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = LxBorderLight,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            // Name field
            Text(
                text = "NAME",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LxTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Your name", color = LxTextSecondary)
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LxPrimary,
                    unfocusedBorderColor = LxBorderLight
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar Icon Picker
            Text(
                text = "AVATAR ICON",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LxTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                symbols.forEach { (symbolName, icon) ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (avatarSymbol == symbolName) LxPrimary
                                else LxBorderLight
                            )
                            .clickable { avatarSymbol = symbolName },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = symbolName,
                            tint = if (avatarSymbol == symbolName) Color.White else LxTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar Color Picker
            Text(
                text = "AVATAR COLOR",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LxTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                colors.forEach { colorHex ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .clickable { avatarColorHex = colorHex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarColorHex == colorHex) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}