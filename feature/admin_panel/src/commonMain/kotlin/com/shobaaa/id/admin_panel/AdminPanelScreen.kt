package com.shobaaa.id.admin_panel

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.shobaaa.id.shared.BebasNeueFont
import com.shobaaa.id.shared.ButtonPrimary
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.IconPrimary
import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.Surface
import com.shobaaa.id.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
  navigateBack: () -> Unit,
  navigateToManageProduct: (String?) -> Unit,
) {

  Scaffold(containerColor = Surface, topBar = {
    TopAppBar(
      title = {
        Text(
          text = "Admin Panel",
          fontFamily = BebasNeueFont(),
          fontSize = FontSize.LARGE,
          color = TextPrimary
        )
      }, navigationIcon = {
        IconButton(onClick = navigateBack) {
          Icon(
            painter = painterResource(Resources.Icon.BackArrow),
            contentDescription = "Back Arrow icon",
            tint = IconPrimary
          )
        }
      }, actions = {
        IconButton(onClick = { }) {
          Icon(
            painter = painterResource(Resources.Icon.Search),
            contentDescription = "Search icon",
            tint = IconPrimary
          )
        }
      }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Surface,
        scrolledContainerColor = Surface,
        navigationIconContentColor = IconPrimary,
        titleContentColor = TextPrimary,
        actionIconContentColor = IconPrimary
      )
    )
  }, floatingActionButton = {
    FloatingActionButton(
      onClick = { navigateToManageProduct(null) },
      containerColor = ButtonPrimary,
      contentColor = IconPrimary,
      content = {
        Icon(
          painter = painterResource(Resources.Icon.Plus), contentDescription = "Add icon"
        )
      })
  }) {

  }
}
