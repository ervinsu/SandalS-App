package com.shobaaa.id.auth.model

import com.shobaaa.id.shared.ViewState

data class AuthState(
  val viewState: ViewState = ViewState.None,
)
