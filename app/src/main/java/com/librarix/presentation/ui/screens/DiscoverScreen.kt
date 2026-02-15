package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.domain.model.SavedBook
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxTextSecondary

@Composable
fun DiscoverScreen(
    onBookClick: (SavedBook) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Discover",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Search and explore books",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )
        }
    }
}