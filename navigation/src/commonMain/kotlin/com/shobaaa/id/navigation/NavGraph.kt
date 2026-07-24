package com.shobaaa.id.navigation

import com.shobaaa.id.manage_product.ManageProductScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shobaaa.id.admin_panel.AdminPanelScreen
import com.shobaaa.id.auth.AuthScreen
import com.shobaaa.id.home.HomeGraphScreen
import com.shobaaa.id.shared.navigation.Screen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.Auth) {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    composable<Screen.Auth> {
      AuthScreen(
        navigateToHome = {
          navController.navigate(Screen.HomeGraph) {
            popUpTo<Screen.Auth> { inclusive = true }
          }
        }
      )
    }

    composable<Screen.HomeGraph> {
      HomeGraphScreen(
        navigateToAuth = {
          navController.navigate(Screen.Auth) {
            popUpTo<Screen.HomeGraph> { inclusive = true }
          }
        },
        navigateToAdmin = {
          navController.navigate(Screen.AdminPanel) {
            popUpTo<Screen.AdminPanel> { inclusive = true }
          }
        }
      )
    }

    composable<Screen.AdminPanel> {
      AdminPanelScreen(
        navigateBack = { navController.navigateUp() },
        navigateToManageProduct = {
          navController.navigate(Screen.ManageProduct(id = it))
        }
      )
    }

    composable<Screen.ManageProduct> {
      val id = it.toRoute<Screen.ManageProduct>().id
      ManageProductScreen(
        id = id, navigateBack = { navController.navigateUp() })
    }
  }
}
