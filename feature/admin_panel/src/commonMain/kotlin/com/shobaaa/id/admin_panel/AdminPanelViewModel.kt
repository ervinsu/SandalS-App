package com.shobaaa.id.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shobaaa.id.data.domain.AdminRepository
import com.shobaaa.id.shared.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

class AdminPanelViewModel(
  private val adminRepository: AdminRepository
) : ViewModel() {

  private val products = adminRepository.loadListProduct(10).stateIn(
      scope = viewModelScope,
      started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
      initialValue = RequestState.Loading
    )

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery

  fun updateSearchQuery(keyword: String) {
    _searchQuery.value = keyword
  }

  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
  val filteredProduct =
    searchQuery.debounce(500.milliseconds).flatMapLatest { query ->
        if (query.isBlank()) {
          products
        } else {
          adminRepository.searchProductByTitle(query)
        }
      }.stateIn(
      scope = viewModelScope,
      started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
      initialValue = RequestState.Loading
    )
}
