package com.shobaaa.id.manage_product

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.gitlive.firebase.storage.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PhotoPicker {

  @Composable
  actual fun InitializePhotoPicker(open: Boolean, onClose: () -> Unit, onImageSelected: (File?) -> Unit) {
    val pickMedia = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
      uri?.let {
        onImageSelected(File(uri))
      } ?: onImageSelected(null)
      onClose()
    }

    LaunchedEffect(open) {
      if (open) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
      }
    }
  }
}
