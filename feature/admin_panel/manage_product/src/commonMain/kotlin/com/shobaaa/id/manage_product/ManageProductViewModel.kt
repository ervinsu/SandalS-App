package com.shobaaa.id.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shobaaa.id.data.domain.AdminRepository
import com.shobaaa.id.shared.domain.Product
import com.shobaaa.id.shared.domain.ProductCategory
import com.shobaaa.id.shared.domain.ProductSize
import com.shobaaa.id.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class ManageProductState(
  val id: String = Uuid.random().toHexString(),
  val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
  val title: String = "",
  val description: String = "",
  val thumbnail: String = "thumbnail image",
  val category: ProductCategory = ProductCategory.Shoes,
  val productSizes: List<ProductSize> = emptyList(),
  val price: Double = 0.0,
  val color: String = "",
  val isNew: Boolean = false,
  val isPopular: Boolean = false,
  val isDiscounted: Boolean = false
)

class ManageProductViewModel(
  private val adminRepository: AdminRepository,
) : ViewModel() {

  var thumbnailUploaderState by mutableStateOf<RequestState<Unit>>(RequestState.Idle)
    private set

  val isFormValid: Boolean
    get() = screenState.title.isNotEmpty() &&
        screenState.description.isNotEmpty() &&
        screenState.price != 0.0 &&
        screenState.thumbnail.isNotEmpty() &&
        screenState.productSizes.isNotEmpty() &&
        screenState.color.isNotEmpty() &&
        screenState.category.name.isNotEmpty()

  var screenState by mutableStateOf(ManageProductState())
    private set

  fun updateId(value: String) {
    screenState = screenState.copy(id = value)
  }

  fun updateCreatedAt(value: Long) {
    screenState = screenState.copy(createdAt = value)
  }

  fun updateTitle(value: String) {
    screenState = screenState.copy(title = value)
  }

  fun updateDescription(value: String) {
    screenState = screenState.copy(description = value)
  }

  fun updateThumbnail(value: String) {
    screenState = screenState.copy(thumbnail = value)
  }

  fun updateThumbnailUploaderState(value: RequestState<Unit>) {
    thumbnailUploaderState = value
  }

  fun updateCategory(value: ProductCategory) {
    screenState = screenState.copy(category = value)
  }

  fun updateSize(value: List<ProductSize>) {
    screenState = screenState.copy(productSizes = value)
  }

  fun updatePrice(value: Double) {
    screenState = screenState.copy(price = value)
  }

  fun updateColor(value: String) {
    screenState = screenState.copy(color = value)
  }

  fun updateNew(value: Boolean) {
    screenState = screenState.copy(isNew = value)
  }

  fun updatePopular(value: Boolean) {
    screenState = screenState.copy(isPopular = value)
  }

  fun updateDiscounted(value: Boolean) {
    screenState = screenState.copy(isDiscounted = value)
  }

  fun createProduct(
    onSuccess: () -> Unit,
    onError: (error: String) -> Unit,
  ) {
    screenState.apply {
      viewModelScope.launch {
        adminRepository.createNewProduct(
          product = Product(
            id = id,
            title = title,
            description = description,
            thumbnail = thumbnail,
            category = category.name,
            size = productSizes,
            price = price,
            color = color
          ), {
            onSuccess()
          }, {
            onError.invoke(it)
          })
      }
    }
  }

  fun uploadThumbnailToStorage(
    file: File?,
    onSuccess: () -> Unit
  ) {
    if (file == null) {
      updateThumbnailUploaderState(RequestState.Error("File is null"))
      return
    }

    updateThumbnailUploaderState(RequestState.Loading)

    viewModelScope.launch {
      try {
        val downloadUrl = adminRepository.uploadImageToStorage(file)

        if (downloadUrl.isNullOrEmpty()) {
          updateThumbnailUploaderState(RequestState.Error("Failed to upload image"))
        } else {
          onSuccess()
          updateThumbnail(downloadUrl)
          updateThumbnailUploaderState(RequestState.Success(Unit))
        }
      } catch (e: Exception) {
        updateThumbnailUploaderState(RequestState.Error(e.message ?: "Unknown error"))
      }
    }
  }
}
