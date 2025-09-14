package com.shobaaa.id.data.domain

import com.shobaaa.id.shared.domain.Customer
import com.shobaaa.id.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

  suspend fun createCustomer(
    user: FirebaseUser?,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  )

  fun getCurrentUserId(): String?

  suspend fun signOut(): RequestState<Unit>

  fun readCustomerFlow(): Flow<RequestState<Customer>>
}
