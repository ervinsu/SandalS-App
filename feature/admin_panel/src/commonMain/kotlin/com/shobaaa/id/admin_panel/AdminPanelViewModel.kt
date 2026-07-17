package com.shobaaa.id.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shobaaa.id.data.domain.AdminRepository
import com.shobaaa.id.shared.util.RequestState
import kotlinx.coroutines.flow.stateIn

class AdminPanelViewModel(
  private val adminRepository: AdminRepository
) : ViewModel() {

  val products = adminRepository.loadListProduct(10).stateIn(
      scope = viewModelScope,
      started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
    initialValue = RequestState.Loading
    )

}
