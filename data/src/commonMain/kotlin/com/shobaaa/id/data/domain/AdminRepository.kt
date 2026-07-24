package com.shobaaa.id.data.domain

import com.shobaaa.id.shared.domain.Product
import com.shobaaa.id.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

  fun getCurrentUserId(): String?

  suspend fun createNewProduct(
    product: Product,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
  )

  suspend fun uploadImageToStorage(file: File): String?

  suspend fun deleteImageFromStorage(
    downloadUrl: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  )

  fun loadListProduct(limit: Int): Flow<RequestState<List<Product>>>

  suspend fun readProductById(id: String): RequestState<Product>
}
