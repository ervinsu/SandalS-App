package com.shobaaa.id.sandals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.shobaaa.id.data.domain.CustomerRepository
import com.shobaaa.id.shared.navigation.Screen
import com.shobaaa.id.navigation.SetupNavGraph
import com.shobaaa.id.shared.Constants
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

import sandals.composeapp.generated.resources.Res
import sandals.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val customerRepository = koinInject<CustomerRepository>()
        var appReady by remember { mutableStateOf(false) }
        val isUserAuthenticated = remember { customerRepository.getCurrentUserId() != null }
        val startDestination = remember {
            if (isUserAuthenticated) Screen.HomeGraph
            else Screen.Auth
        }

        LaunchedEffect(Unit) {
            GoogleAuthProvider.create(
                credentials = GoogleAuthCredentials(serverId = Constants.WEB_CLIENT_ID)
            )
            appReady = true
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = appReady
        ) {
            SetupNavGraph(startDestination)
        }
    }
}
