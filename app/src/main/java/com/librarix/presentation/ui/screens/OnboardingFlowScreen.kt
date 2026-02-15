package com.librarix.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarix.presentation.ui.theme.LxBackgroundLight
import com.librarix.presentation.ui.theme.LxBorderLight
import com.librarix.presentation.ui.theme.LxPrimary
import com.librarix.presentation.ui.theme.LxSurfaceLight
import com.librarix.presentation.ui.theme.LxTextSecondary

@Composable
fun OnboardingFlowScreen(
    onComplete: (String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isNameValid = name.trim().length >= 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            // Progress indicator
            ProgressIndicator(currentStep = currentStep, totalSteps = 2)

            Spacer(modifier = Modifier.height(18.dp))

            // Content
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    0 -> WelcomeStep(
                        onGetStarted = {
                            currentStep = 1
                        }
                    )
                    1 -> ProfileStep(
                        name = name,
                        onNameChange = {
                            name = it
                            errorMessage = null
                        },
                        isNameValid = isNameValid,
                        errorMessage = errorMessage,
                        onContinue = {
                            if (isNameValid) {
                                currentStep = 2
                            }
                        }
                    )
                    2 -> DoneStep(
                        name = name.trim(),
                        isLoading = isLoading,
                        onComplete = {
                            isLoading = true
                            // Simulate completion
                            onComplete(name.trim())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    currentStep: Int,
    totalSteps: Int
) {
    Column {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalSteps + 1) { index ->
                val isComplete = index <= currentStep
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(
                            color = if (isComplete) LxPrimary else LxTextSecondary.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun WelcomeStep(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = LxPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(22.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Lx",
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                color = LxPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome to Librarix",
            fontWeight = FontWeight.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Track your reading, keep your notes, and build momentum every day.",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = LxTextSecondary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Get Started",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ProfileStep(
    name: String,
    onNameChange: (String) -> Unit,
    isNameValid: Boolean,
    errorMessage: String?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Set up your profile",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose how your name appears in the app.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = LxTextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "NAME",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = LxTextSecondary
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
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

            if (name.isNotEmpty() && !isNameValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Name must be at least 2 characters.",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = isNameValid,
            colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Continue",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun DoneStep(
    name: String,
    isLoading: Boolean,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You're all set, $name!",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start adding books to your library.",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = LxTextSecondary
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = LxPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Start Reading",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}