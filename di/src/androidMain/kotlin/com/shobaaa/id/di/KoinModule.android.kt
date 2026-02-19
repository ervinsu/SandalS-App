package com.shobaaa.id.di

import com.shobaaa.id.manage_product.PhotoPicker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val  targetModule: Module = module {
    single<PhotoPicker> { PhotoPicker() }
}
