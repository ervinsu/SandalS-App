package com.shobaaa.id.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shobaaa.id.auth.AuthScreen
import com.shobaaa.id.auth.component.GoogleButton

@Preview(showBackground = true)
@Composable
private fun GoogleButtonPreview() {
  GoogleButton {}
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
  AuthScreen()
}
