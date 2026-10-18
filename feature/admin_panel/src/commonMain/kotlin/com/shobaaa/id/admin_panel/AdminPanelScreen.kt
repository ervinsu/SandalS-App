package com.shobaaa.id.admin_panel

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shobaaa.id.shared.BebasNeueFont
import com.shobaaa.id.shared.BorderIdle
import com.shobaaa.id.shared.ButtonPrimary
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.IconPrimary
import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.Surface
import com.shobaaa.id.shared.SurfaceLighter
import com.shobaaa.id.shared.TextPrimary
import com.shobaaa.id.shared.component.InfoCard
import com.shobaaa.id.shared.component.LoadingCard
import com.shobaaa.id.shared.component.ProductCard
import com.shobaaa.id.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sandals.shared.generated.resources.Res
import sandals.shared.generated.resources.admin_panel
import sandals.shared.generated.resources.cat

@OptIn(ExperimentalMaterial3Api::class) @Composable fun AdminPanelScreen(
  navigateBack: () -> Unit,
  navigateToManageProduct: (String?) -> Unit,
) {
  val viewModel = koinViewModel<AdminPanelViewModel>()
  val products = viewModel.filteredProduct.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var searchBarVisible by mutableStateOf(false)

  Scaffold(containerColor = Surface, topBar = {
    AnimatedContent(
      targetState = searchBarVisible
    ) { visible ->
      if (visible) {
        SearchBar(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          inputField = {
            SearchBarDefaults.InputField(
              modifier = Modifier.fillMaxWidth(),
              query = searchQuery,
              onQueryChange = viewModel::updateSearchQuery,
              expanded = false,
              onExpandedChange = {},
              onSearch = {},
              placeholder = {
                Text(
                  text = "Search here",
                  fontSize = FontSize.REGULAR,
                  color = TextPrimary
                )
              },
              trailingIcon = {
                IconButton(
                  modifier = Modifier.size(14.dp),
                  onClick = {
                    if (searchQuery.isNotEmpty()) viewModel.updateSearchQuery("")
                    else searchBarVisible = false
                  }
                ) {
                  Icon(
                    painter = painterResource(Resources.Icon.Close),
                    contentDescription = "Close icon"
                  )
                }
              }
            )
          },
          colors = SearchBarColors(
            containerColor = SurfaceLighter, dividerColor = BorderIdle
          ),
          expanded = false,
          onExpandedChange = {},
          content = {}
        )
      } else {
        TopAppBar(
          title = {
            Text(
              text = stringResource(Res.string.admin_panel),
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
            IconButton(onClick = {

            }) {
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
      }
    }
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
  }) { paddingValues ->
    products.value.DisplayResult(
      modifier = Modifier.padding(
      top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding()
    ), onLoading = {
      LoadingCard(modifier = Modifier.fillMaxSize())
    }, onSuccess = { products ->
      AnimatedContent(
        targetState = products
      ) { products ->
        if (products.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(
              items = products, key = { it.id }) { product ->
              ProductCard(
                product = product, onClick = {
                  navigateToManageProduct(product.id)
                })
            }
          }
        } else {
          InfoCard(
            image = Resources.Image.Cat, title = "Oops!", subtitle = "Products not found."
          )
        }
      }
    }, onError = {
      InfoCard(
        image = Res.drawable.cat,
        title = "Oops!",
        subtitle = it,
        modifier = Modifier.fillMaxSize()
      )
    })

  }
}
