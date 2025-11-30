package com.shobaaa.id.shared.domain

import androidx.compose.ui.graphics.Color
import com.shobaaa.id.shared.CategoryBlue
import com.shobaaa.id.shared.CategoryYellow
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class Product(
  val id: String,
  val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
  val title: String,
  val description: String,
  val thumbnail: String,
  val category: String,
  val colors: List<String>? = null,
  val size: Int? = null,
  val stock: Int = 0,
  val price: Double,
  val isPopular: Boolean = false,
  val isDiscounted: Boolean = false,
  val isNew: Boolean = false
)

enum class ProductCategory(
  val title: String,
  val color: Color
) {
  Shoes(
    title = "Sepatu",
    color = CategoryYellow
  ),
  Bag(
    title = "Tas",
    color = CategoryBlue
  ),
}
