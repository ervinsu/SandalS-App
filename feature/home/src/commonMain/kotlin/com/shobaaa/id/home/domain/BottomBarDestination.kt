package com.shobaaa.id.home.domain

import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
  val icon: DrawableResource,
  val title: String,
  val screen: Screen
) {
  ProductOverview(
    icon = Resources.Icon.Home,
    title = "SandalS",
    screen = Screen.ProductsOverview
  ),
  Cart(
    icon = Resources.Icon.ShoppingCart,
    title = "Cart",
    screen = Screen.Cart
  ),
  Categories(
    icon = Resources.Icon.Categories,
    title = "Categories",
    screen = Screen.Categories
  )

}
