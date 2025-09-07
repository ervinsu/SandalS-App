package com.shobaaa.id.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

  @Serializable
  data object Auth: Screen()
}
