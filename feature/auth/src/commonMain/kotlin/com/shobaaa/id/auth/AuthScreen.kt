package com.shobaaa.id.auth

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.shobaaa.id.auth.component.GoogleButton
import com.shobaaa.id.shared.Alpha
import com.shobaaa.id.shared.BebasNeueFont
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.Surface
import com.shobaaa.id.shared.SurfaceBrand
import com.shobaaa.id.shared.SurfaceError
import com.shobaaa.id.shared.TextPrimary
import com.shobaaa.id.shared.TextSecondary
import com.shobaaa.id.shared.TextWhite
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun AuthScreen() {
  val viewmodel = koinViewModel<AuthViewModel>()
  val messageBarState = rememberMessageBarState()
  var loadingState by remember { mutableStateOf(false) }

  Scaffold { padding ->
    ContentWithMessageBar(
      modifier = Modifier
        .padding(
          top = padding.calculateTopPadding(),
          bottom = padding.calculateBottomPadding()
        ),
      messageBarState = messageBarState,
      errorMaxLines = 2,
      errorContentColor = TextWhite,
      errorContainerColor = SurfaceError,
      successContainerColor = SurfaceBrand,
      successContentColor = TextPrimary

    ) {
      Column(modifier = Modifier.fillMaxSize().padding(all = 24.dp)) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = "SandalS",
            textAlign = TextAlign.Center,
            fontFamily = BebasNeueFont(),
            fontSize = FontSize.EXTRA_LARGE,
            color = TextSecondary
          )
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .alpha(Alpha.HALF),
            text = "Sign in to continue",
            textAlign = TextAlign.Center,
            fontSize = FontSize.EXTRA_REGULAR,
            color = TextPrimary
          )
        }

        GoogleButtonUiContainerFirebase(
          linkAccount = false, onResult = { result ->
            result.onSuccess { user ->
              viewmodel.createCustomer(
                user = user,
                onSuccess = {
                  messageBarState.addSuccess("Authentication successful!")
                },
                onError = { msg ->
                  messageBarState.addError(msg)
                }
              )
              loadingState = false
            }.onFailure { error ->
              when {
                error.message?.contains("A network error") == true -> {
                  messageBarState.addError("Internet connection unavailable.")
                }
                error.message?.contains("Idtoken is null") == true -> {
                  messageBarState.addError("Sign in canceled.")
                }
                else -> {
                  messageBarState.addError(error.message ?: "Unknown")
                }
              }
              loadingState = false
            }
          }) {
          GoogleButton(loading = loadingState) {
            loadingState = true
            this@GoogleButtonUiContainerFirebase.onClick()
          }
        }
      }
    }
  }
}
