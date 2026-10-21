package com.shobaaa.id.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shobaaa.id.MainProductCard
import com.shobaaa.id.shared.Alpha
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.TextPrimary
import com.shobaaa.id.shared.component.InfoCard
import com.shobaaa.id.shared.component.LoadingCard
import com.shobaaa.id.shared.component.ProductCard
import com.shobaaa.id.shared.domain.Product
import com.shobaaa.id.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductOverviewScreen(
  navigateToDetails: (String) -> Unit,
) {

  val viewModel = koinViewModel<ProductOverviewViewModel>()
  val products by viewModel.products.collectAsState()
  val listState = rememberLazyListState()

  val centeredIndex: Int? by remember {
    derivedStateOf {
      val layoutInfo = listState.layoutInfo
      val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
      layoutInfo.visibleItemsInfo.minByOrNull { item ->
        val itemCenter = item.offset + item.size / 2
        kotlin.math.abs(itemCenter - viewportCenter)
      }?.index
    }
  }

  products.DisplayResult(
    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
    onSuccess = { productList ->
      AnimatedContent(
        targetState = productList.distinctBy { it.id }) { products ->
        if (products.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier.fillMaxSize()
          ) {
            item(key = "new_products") {
              LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                itemsIndexed(items = products.filter { it.isNew }.sortedBy { it.createdAt }.take(6),
                  key = { _, item -> item.id }) { index, product ->
                  val isLarge = index == centeredIndex
                  val animatedScale by animateFloatAsState(
                    targetValue = if (isLarge) 1f else 0.8f, animationSpec = tween(300)
                  )

                  MainProductCard(
                    modifier = Modifier.scale(animatedScale).height(300.dp).fillParentMaxWidth(0.6f),
                    product = product,
                    isLarge = isLarge,
                    onClick = { navigateToDetails(product.id) })
                }
              }
            }
            item(key = "discounted_header") {
              Text(
                modifier = Modifier.fillMaxWidth()
                  .padding(top = 24.dp, bottom = 16.dp)
                  .alpha(Alpha.HALF),
                text = "Discounted Products",
                fontSize = FontSize.EXTRA_REGULAR,
                color = TextPrimary,
                textAlign = TextAlign.Center
              )
            }
            items(items = products.filter { it.isDiscounted }.sortedBy { it.createdAt }
              .take(3), key = { it.id }) { product ->
              ProductCard(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                product = product, onClick = { navigateToDetails(it) })
            }
          }
        } else {
          InfoCard(
            image = Resources.Image.Cat, title = "Nothing here", subtitle = "Empty product list."
          )
        }
      }
    },
    onError = { message ->
      InfoCard(
        image = Resources.Image.Cat, title = "Oops!", subtitle = message
      )
    })
}
