package com.shobaaa.id.sandals

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.shobaaa.id.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {

  override fun onCreate() {
    super.onCreate()
    initializeKoin(
      config = {
          androidContext(this@MyApplication)
      }
    )
    Firebase.initialize(context = this)
  }
}
