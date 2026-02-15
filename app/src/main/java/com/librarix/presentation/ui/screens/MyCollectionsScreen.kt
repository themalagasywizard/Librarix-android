package com.librarix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

data class UserCollection(
    val id: String,
    val name: String,
    val bookCount: Int = 0,
    val coverUrls: List<String> = emptyList()
)

@Composable
fun MyCollectionsScreen(
    collections: List<UserCollection> = emptyList(),
    onCreateCollection: (String) -> Unit,
    onCollectionClick: (UserCollection) -> Unit
) {
    var newCollectionName by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "MY COLLECTIONS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Create new collection
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = newCollectionName,
                onValueChange = { newCollectionName = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Create a collection...",
                        color = LxTextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LxPrimary,
                    unfocusedBorderColor = LxBorderLight
                )
            )

            Button(
                onClick = {
                    if (newCollectionName.isNotBlank()) {
                        isCreating = true
                        onCreateCollection(newCollectionName)
                        newCollectionName = ""
                        isCreating = false
                    }
                },
                enabled = newCollectionName.isNotBlank() && !isCreating,
                colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Collections list
        if (collections.isEmpty()) {
            EmptyState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(collections) { collection ->
                    CollectionCard(
                        collection = collection,
                        onClick = { onCollectionClick(collection) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Create your first collection to start saving books.",
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = LxTextSecondary
        )
    }
}

@Composable
private fun CollectionCard(
    collection: UserCollection,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LxSurfaceLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Cover grid placeholder
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .background(
                                color = LxBorderLight,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = collection.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 1
            )

            Text(
                text = "${collection.bookCount} books",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = LxTextSecondary
            )
        }
    }
}