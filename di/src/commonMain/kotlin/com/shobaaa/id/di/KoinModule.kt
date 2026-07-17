package com.shobaaa.id.di

import com.shobaaa.id.admin_panel.AdminPanelViewModel
import com.shobaaa.id.auth.AuthViewModel
import com.shobaaa.id.data.AdminRepositoryImpl
import com.shobaaa.id.data.CustomerRepositoryImpl
import com.shobaaa.id.data.domain.AdminRepository
import com.shobaaa.id.data.domain.CustomerRepository
import com.shobaaa.id.home.HomeGraphViewModel
import com.shobaaa.id.manage_product.ManageProductViewModel
import com.shobaaa.id.manage_product.PhotoPicker
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
  single<CustomerRepository> { CustomerRepositoryImpl() }
  single<AdminRepository> { AdminRepositoryImpl() }
  viewModelOf(::AuthViewModel)
  viewModelOf(::ManageProductViewModel)
  viewModelOf(::HomeGraphViewModel)
  viewModelOf(::AdminPanelViewModel)
}

expect val targetModule: Module

fun initializeKoin(
  config: (KoinApplication.() -> Unit)? = null
) {
  startKoin {
    config?.invoke(this)
    modules(sharedModule, targetModule)
  }

}
