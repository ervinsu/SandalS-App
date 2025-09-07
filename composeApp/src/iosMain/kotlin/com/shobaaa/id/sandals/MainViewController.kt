package com.shobaaa.id.sandals

import androidx.compose.ui.window.ComposeUIViewController
import com.shobaaa.id.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
  configure = {
    initializeKoin()
  }
) { App() }
