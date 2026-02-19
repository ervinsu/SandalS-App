package com.shobaaa.id.shared.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.shobaaa.id.shared.Alpha
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.Surface
import com.shobaaa.id.shared.TextPrimary
import com.shobaaa.id.shared.TextSecondary
import com.shobaaa.id.shared.component.CustomTextField
import com.shobaaa.id.shared.component.PrimaryButton
import com.shobaaa.id.shared.domain.ProductCategory
import com.shobaaa.id.shared.domain.ProductSize
import org.jetbrains.compose.resources.stringResource
import rememberMessageBarState
import sandals.shared.generated.resources.Res
import sandals.shared.generated.resources.add_new_size
import sandals.shared.generated.resources.cancel
import sandals.shared.generated.resources.error_edit_size
import sandals.shared.generated.resources.ok
import sandals.shared.generated.resources.select_size
import sandals.shared.generated.resources.size
import sandals.shared.generated.resources.stock

@Composable
fun SizeDialog(
  productSizes: List<ProductSize>,
  onDismiss: () -> Unit,
  onConfirmClick: (List<ProductSize>) -> Unit,
  productCategory: ProductCategory
) {
  val currentSizes = remember { productSizes.toMutableStateList() }
  val showErrorDialog = remember { mutableStateOf(false) }

  fun checkSave(): Boolean {
    return if (productCategory == ProductCategory.Bag) {
      currentSizes.all { (_, stock) -> stock != 0 }
    } else {
      currentSizes.all { (size, stock) -> size?.let { it != 0}?: false && stock != 0 }
    }
  }

  AlertDialog(
    properties = DialogProperties(
      usePlatformDefaultWidth = false
    ),
    containerColor = Surface,
    modifier = Modifier
      .fillMaxWidth(0.9f)
      .imePadding()
      .padding(vertical = 24.dp),
    title = {
      Text(
        text = stringResource(Res.string.select_size),
        fontSize = FontSize.EXTRA_MEDIUM,
        color = TextPrimary
      )
    },
    text = {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(size = 6.dp)).padding(
            vertical = 16.dp, horizontal = 12.dp
          ), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (productCategory == ProductCategory.Shoes) {
            Text(
              modifier = Modifier.weight(1f),
              text = stringResource(Res.string.size),
              fontSize = FontSize.REGULAR,
              fontWeight = FontWeight.Medium
            )
          }
          Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.stock),
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium
          )
        }
        LazyColumn (modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(size = 6.dp))) {
          itemsIndexed(currentSizes) { index, currentSize ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)
            ) {
              if (productCategory == ProductCategory.Shoes) {
                CustomTextField(
                  modifier = Modifier.weight(1f),
                  value = "${if ((currentSize.size ?: 0) == 0) "" else currentSize.size}",
                  onValueChange = {
                    currentSizes[index] = currentSize.copy(size = it.toIntOrNull() ?: 0)
                  },
                  placeholder = stringResource(Res.string.size),
                  keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                  )
                )
              }

              CustomTextField(
                modifier = Modifier.weight(1f),
                value = "${if (currentSize.stock == 0) "" else currentSize.stock}",
                onValueChange = {
                  currentSizes[index] = currentSize.copy(stock = it.toIntOrNull() ?: 0)
                },
                placeholder = stringResource(Res.string.stock),
                keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Number
                )
              )
            }
          }

          if (productCategory == ProductCategory.Shoes || currentSizes.isEmpty()) {
            item {
              PrimaryButton(
                text = stringResource(Res.string.add_new_size),
                icon = Resources.Icon.Plus,
                enabled = true,
                onClick = {
                  currentSizes.add(ProductSize())
                })
            }
          }
        }

        if (showErrorDialog.value) {
          Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.error_edit_size),
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium,
            color = Color.Red
          )
        }
      }
    },
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(
        onClick = {
          if (checkSave()) {
            onConfirmClick(currentSizes)
          } else {
            showErrorDialog.value = true
          }
        },
        colors = ButtonDefaults.textButtonColors(
          containerColor = Color.Transparent,
          contentColor = TextSecondary
        )
      ) {
        Text(
          text = stringResource(Res.string.ok),
          fontSize = FontSize.REGULAR,
          fontWeight = FontWeight.Medium
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        colors = ButtonDefaults.textButtonColors(
          containerColor = Color.Transparent,
          contentColor = TextPrimary.copy(alpha = Alpha.HALF)
        )
      ) {
        Text(
          text = stringResource(Res.string.cancel),
          fontSize = FontSize.REGULAR,
          fontWeight = FontWeight.Medium
        )
      }
    }
  )
}
