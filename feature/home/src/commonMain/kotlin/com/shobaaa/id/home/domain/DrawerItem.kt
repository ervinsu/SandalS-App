package com.shobaaa.id.home.domain

import com.shobaaa.id.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem(
  val title: String,
  val icon: DrawableResource
) {
  Profile(
    title = "Profile",
    icon = Resources.Icon.Person
  ),
  Blog(
    title = "Blog",
    icon = Resources.Icon.Book
  ),
  Locations(
    title = "Location",
    icon = Resources.Icon.ShoppingCart
  ),
  Contact(
    title = "Contact Us",
    icon = Resources.Icon.Edit
  ),
  SignOut(
    title = "Sign out",
    icon = Resources.Icon.SignOut
  ),
  Admin(
    title = "Admin Panel",
    icon = Resources.Icon.Unlock
  )
}
