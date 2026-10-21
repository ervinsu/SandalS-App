package com.shobaaa.id.data

import com.shobaaa.id.data.domain.ProductRepository
import com.shobaaa.id.shared.domain.Product
import com.shobaaa.id.shared.domain.ProductCategory
import com.shobaaa.id.shared.domain.ProductSize
import com.shobaaa.id.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductRepositoryImpl : ProductRepository {
  init {
    println("Initializing....ProductRepository")
//    threadSleep(2000)
  }
  override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

  override fun readDiscountedProducts(): Flow<RequestState<List<Product>>> = channelFlow {
    try {
      val userId = getCurrentUserId()
      if (userId != null) {
        val database = Firebase.firestore
        database.collection(collectionPath = "product")
          .where { "isDiscounted" equalTo true }
          .snapshots
          .collectLatest { query ->
            val products = query.documents.map { productDoc ->
              Product(
                id = productDoc.id,
                title = productDoc.get(field = "title"),
                createdAt = productDoc.get(field = "createdAt"),
                description = productDoc.get(field = "description"),
                thumbnail = productDoc.get(field = "thumbnail"),
                category = productDoc.get(field = "category"),
                totalStock = productDoc.get(field = "totalStock"),
                size = productDoc.get<List<ProductSize>?>(field = "size"),
                price = productDoc.get(field = "price"),
                isPopular = productDoc.get(field = "isPopular"),
                isDiscounted = productDoc.get(field = "isDiscounted"),
                isNew = productDoc.get(field = "isNew"),
                color = productDoc.get(field = "color")
              )
            }
            send(RequestState.Success(data = products.map { it.copy(title = it.title.uppercase()) }))
          }
      } else {
        send(RequestState.Error("User is not available."))
      }
    } catch (e: Exception) {
      send(RequestState.Error("Error while reading the last 10 items from the database: ${e.message}"))
    }
  }

  override fun readNewProducts(): Flow<RequestState<List<Product>>> = channelFlow {
    try {
      val userId = getCurrentUserId()
      if (userId != null) {
        val database = Firebase.firestore
        database.collection(collectionPath = "product")
          .where { "isNew" equalTo true }
          .snapshots
          .collectLatest { query ->
            val products = query.documents.map { productDoc ->
              Product(
                id = productDoc.id,
                title = productDoc.get(field = "title"),
                createdAt = productDoc.get(field = "createdAt"),
                description = productDoc.get(field = "description"),
                thumbnail = productDoc.get(field = "thumbnail"),
                category = productDoc.get(field = "category"),
                totalStock = productDoc.get(field = "totalStock"),
                size = productDoc.get<List<ProductSize>?>(field = "size"),
                price = productDoc.get(field = "price"),
                isPopular = productDoc.get(field = "isPopular"),
                isDiscounted = productDoc.get(field = "isDiscounted"),
                isNew = productDoc.get(field = "isNew"),
                color = productDoc.get(field = "color")
              )
            }
            send(RequestState.Success(data = products.map { it.copy(title = it.title.uppercase()) }))
          }
      } else {
        send(RequestState.Error("User is not available."))
      }
    } catch (e: Exception) {
      send(RequestState.Error("Error while reading the last 10 items from the database: ${e.message}"))
    }
  }

  override fun readProductByIdFlow(id: String): Flow<RequestState<Product>> = channelFlow {
    try {
      val userId = getCurrentUserId()
      if (userId != null) {
        val database = Firebase.firestore
        database.collection(collectionPath = "product")
          .document(id)
          .snapshots
          .collectLatest { productDoc ->
            if (productDoc.exists) {
              val product = Product(
                id = productDoc.id,
                title = productDoc.get(field = "title"),
                createdAt = productDoc.get(field = "createdAt"),
                description = productDoc.get(field = "description"),
                thumbnail = productDoc.get(field = "thumbnail"),
                category = productDoc.get(field = "category"),
                totalStock = productDoc.get(field = "totalStock"),
                size = productDoc.get<List<ProductSize>?>(field = "size"),
                price = productDoc.get(field = "price"),
                isPopular = productDoc.get(field = "isPopular"),
                isDiscounted = productDoc.get(field = "isDiscounted"),
                isNew = productDoc.get(field = "isNew"),
                color = productDoc.get(field = "color")
              )
              send(RequestState.Success(product.copy(title = product.title.uppercase())))
            } else {
              send(RequestState.Error("Selected product does not exist."))
            }
          }
      } else {
        send(RequestState.Error("User is not available."))
      }
    } catch (e: Exception) {
      send(RequestState.Error("Error while reading a selected product: ${e.message}"))
    }
  }

  override fun readProductsByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>> =
    channelFlow {
      try {
        val userId = getCurrentUserId()
        if (userId != null) {
          val database = Firebase.firestore
          val productCollection = database.collection(collectionPath = "product")

          val allProducts = mutableListOf<Product>()
          val chunks = ids.chunked(10)

          chunks.forEachIndexed { index, chunk ->
            productCollection
              .where { "id" inArray chunk }
              .snapshots
              .collectLatest { query ->
                val products = query.documents.map { productDoc ->
                  Product(
                    id = productDoc.id,
                    title = productDoc.get(field = "title"),
                    createdAt = productDoc.get(field = "createdAt"),
                    description = productDoc.get(field = "description"),
                    thumbnail = productDoc.get(field = "thumbnail"),
                    category = productDoc.get(field = "category"),
                    totalStock = productDoc.get(field = "totalStock"),
                    size = productDoc.get<List<ProductSize>?>(field = "size"),
                    price = productDoc.get(field = "price"),
                    isPopular = productDoc.get(field = "isPopular"),
                    isDiscounted = productDoc.get(field = "isDiscounted"),
                    isNew = productDoc.get(field = "isNew"),
                    color = productDoc.get(field = "color")
                  )
                }
                allProducts.addAll(products.map { it.copy(title = it.title.uppercase()) })

                if (index == chunks.lastIndex) {
                  send(RequestState.Success(allProducts))
                }
              }
          }
        } else {
          send(RequestState.Error("User is not available."))
        }
      } catch (e: Exception) {
        send(RequestState.Error("Error while reading a selected product: ${e.message}"))
      }
    }

  override fun readProductsByCategoryFlow(category: ProductCategory): Flow<RequestState<List<Product>>> =
    channelFlow {
      try {
        val userId = getCurrentUserId()
        if (userId != null) {
          val database = Firebase.firestore
          database.collection(collectionPath = "product")
            .where { "category" equalTo category.name }
            .snapshots
            .collectLatest { query ->
              val products = query.documents.map { productDoc ->
                Product(
                  id = productDoc.id,
                  title = productDoc.get(field = "title"),
                  createdAt = productDoc.get(field = "createdAt"),
                  description = productDoc.get(field = "description"),
                  thumbnail = productDoc.get(field = "thumbnail"),
                  category = productDoc.get(field = "category"),
                  totalStock = productDoc.get(field = "totalStock"),
                  size = productDoc.get<List<ProductSize>?>(field = "size"),
                  price = productDoc.get(field = "price"),
                  isPopular = productDoc.get(field = "isPopular"),
                  isDiscounted = productDoc.get(field = "isDiscounted"),
                  isNew = productDoc.get(field = "isNew"),
                  color = productDoc.get(field = "color")
                )
              }
              send(RequestState.Success(products.map { it.copy(title = it.title.uppercase()) }))
            }
        } else {
          send(RequestState.Error("User is not available."))
        }
      } catch (e: Exception) {
        send(RequestState.Error("Error while reading a selected product: ${e.message}"))
      }
    }
}
