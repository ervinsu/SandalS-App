import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.shobaaa.id.manage_product.ManageProductViewModel
import com.shobaaa.id.manage_product.PhotoPicker
import com.shobaaa.id.shared.BebasNeueFont
import com.shobaaa.id.shared.BorderIdle
import com.shobaaa.id.shared.ButtonPrimary
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.IconPrimary
import com.shobaaa.id.shared.Resources
import com.shobaaa.id.shared.Surface
import com.shobaaa.id.shared.SurfaceBrand
import com.shobaaa.id.shared.SurfaceError
import com.shobaaa.id.shared.SurfaceLighter
import com.shobaaa.id.shared.TextPrimary
import com.shobaaa.id.shared.TextSecondary
import com.shobaaa.id.shared.TextWhite
import com.shobaaa.id.shared.component.AlertTextField
import com.shobaaa.id.shared.component.CustomTextField
import com.shobaaa.id.shared.component.ErrorCard
import com.shobaaa.id.shared.component.LoadingCard
import com.shobaaa.id.shared.component.PrimaryButton
import com.shobaaa.id.shared.component.dialog.CategoriesDialog
import com.shobaaa.id.shared.component.dialog.SizeDialog
import com.shobaaa.id.shared.domain.ProductCategory
import com.shobaaa.id.shared.util.DisplayResult
import com.shobaaa.id.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import sandals.shared.generated.resources.Res
import sandals.shared.generated.resources.add_new_product
import sandals.shared.generated.resources.color
import sandals.shared.generated.resources.description
import sandals.shared.generated.resources.edit_product
import sandals.shared.generated.resources.new_product
import sandals.shared.generated.resources.price
import sandals.shared.generated.resources.size
import sandals.shared.generated.resources.stock
import sandals.shared.generated.resources.success_add_product
import sandals.shared.generated.resources.title

@OptIn(ExperimentalMaterial3Api::class) @Composable
fun ManageProductScreen(
  id: String?,
  navigateBack: () -> Unit
) {

  val messageBarState = rememberMessageBarState()
  val viewModel = koinViewModel<ManageProductViewModel>()
  val screenState = viewModel.screenState
  var showCategoriesDialog by remember { mutableStateOf(false) }
  var showSizeDialog by remember { mutableStateOf(false) }
  val isFormValid = viewModel.isFormValid
  val thumbnailUploaded = viewModel.thumbnailUploaderState
  val successAddProduct = stringResource(Res.string.success_add_product)
  val photoPicker = koinInject<PhotoPicker>()
  var openImagePicker by remember { mutableStateOf(false) }

  photoPicker.InitializePhotoPicker(
    open = openImagePicker,
    onClose = {
      openImagePicker = false
    },
    onImageSelected = { file ->
      viewModel.uploadThumbnailToStorage(
        file = file,
        onSuccess = {
          messageBarState.addSuccess("Thumbnail uploaded successfully.")
        }
      )
    }
  )

  AnimatedVisibility(
    visible = showCategoriesDialog
  ) {
    CategoriesDialog(
      category = screenState.category,
      onDismiss = {
        showCategoriesDialog = false
      },
      onConfirmClick = {
        viewModel.updateCategory(it)
        showCategoriesDialog = false
      }
    )
  }

  AnimatedVisibility(
    visible = showSizeDialog
  ) {
    SizeDialog(
      productSizes = screenState.productSizes,
      onDismiss = {
        showSizeDialog = false
      },
      onConfirmClick = {
        viewModel.updateSize(it)
        showSizeDialog = false
      },
      productCategory = screenState.category
    )
  }

  Scaffold(containerColor = Surface, topBar = {
    TopAppBar(
      title = {
        Text(
          text = if (id == null) {
            stringResource(Res.string.new_product)
          } else {
            stringResource(Res.string.edit_product)
          },
          fontFamily = BebasNeueFont(),
          fontSize = FontSize.LARGE,
          color = TextPrimary
        )
      }, navigationIcon = {
        IconButton(onClick = navigateBack) {
          Icon(
            painter = painterResource(Resources.Icon.BackArrow),
            contentDescription = "Back Arrow icon",
            tint = IconPrimary
          )
        }
      }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Surface,
        scrolledContainerColor = Surface,
        navigationIconContentColor = IconPrimary,
        titleContentColor = TextPrimary,
        actionIconContentColor = IconPrimary
      )
    )
  }
  ) { padding ->
    ContentWithMessageBar(
      modifier = Modifier
        .padding(
          top = padding.calculateTopPadding(),
          bottom = padding.calculateBottomPadding()
        ),
      contentBackgroundColor = Surface,
      messageBarState = messageBarState,
      errorMaxLines = 2,
      errorContainerColor = SurfaceError,
      errorContentColor = TextWhite,
      successContainerColor = SurfaceBrand,
      successContentColor = TextPrimary
    ) {

      Column(
        modifier = Modifier
          .padding(horizontal = 24.dp)
          .padding(
            bottom = 24.dp,
            top = 12.dp
          )
          .imePadding()
      ) {

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
              .clip(RoundedCornerShape(size = 12.dp))
              .border(
                width = 1.dp,
                color = BorderIdle,
                shape = RoundedCornerShape(size = 12.dp)
              )
              .background(SurfaceLighter)
              .clickable(enabled = thumbnailUploaded.isIdle()) {
                openImagePicker = true
              },
            contentAlignment = Alignment.Center
          ) {
            thumbnailUploaded.DisplayResult(
              onIdle = {
                Icon(
                  modifier = Modifier.size(24.dp),
                  painter = painterResource(Resources.Icon.Plus),
                  contentDescription = "Plus Icon",
                  tint = IconPrimary
                )
              },
              onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
              },
              onSuccess = {
                AsyncImage(
                  modifier = Modifier.fillMaxSize(),
                  model = ImageRequest.Builder(
                    LocalPlatformContext.current
                  ).data(screenState.thumbnail)
                    .crossfade(enable = true)
                    .build(),
                  contentDescription = "Product thumbnail image",
                  contentScale = ContentScale.Crop
                )
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(size = 6.dp))
                    .padding(
                      top = 12.dp,
                      end = 12.dp
                    )
                    .background(ButtonPrimary)
                    .clickable {
//                      viewModel.deleteThumbnailFromStorage(
//                        onSuccess = { messageBarState.addSuccess("Thumbnail removed successfully.") },
//                        onError = { message ->
//                          messageBarState.addError(
//                            message
//                          )
//                        }
//                      )
                    }
                    .padding(all = 12.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(Resources.Icon.Delete),
                    contentDescription = "Delete icon"
                  )
                }
              },
              onError = { message ->
                Column(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  ErrorCard(message = message)
                  Spacer(modifier = Modifier.height(12.dp))
                  TextButton(
                    onClick = {
                      viewModel.updateThumbnailUploaderState(RequestState.Idle)
                    },
                    colors = ButtonDefaults.textButtonColors(
                      containerColor = Color.Transparent
                    )
                  ) {
                    Text(
                      text = "Try again",
                      fontSize = FontSize.SMALL,
                      color = TextSecondary
                    )
                  }
                }
              }
            )
          }
          TextField(
            value = screenState.title,
            onValueChange = {},
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Characters,
              autoCorrect = false,
          keyboardType = KeyboardType.Text
          )
          )
          CustomTextField(
            value = screenState.title,
            onValueChange = { viewModel.updateTitle(it) },
            placeholder = stringResource(Res.string.title)
          )
          CustomTextField(
            modifier = Modifier.height(168.dp),
            value = screenState.description,
            onValueChange = { viewModel.updateDescription(it) },
            placeholder = stringResource(Res.string.description),
            expanded = true
          )
          AlertTextField(
            modifier = Modifier.fillMaxWidth(),
            text = screenState.category.title,
            onClick = {
              showCategoriesDialog = true
            }
          )
          if (screenState.productSizes.isNotEmpty()) {
            Column (Modifier.clickable(onClick = {
              showSizeDialog = true
            })) {
              Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(size = 6.dp)).padding(
                  vertical = 16.dp, horizontal = 12.dp
                ), horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                if (screenState.category == ProductCategory.Shoes) {
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
              screenState.productSizes.forEach {
                Row(
                  modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(size = 6.dp)).padding(
                    vertical = 16.dp, horizontal = 12.dp
                  ).border(1.dp, BorderIdle, RoundedCornerShape(size = 6.dp)), horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  if (screenState.category == ProductCategory.Shoes) {
                    Text(
                      modifier = Modifier.weight(1f),
                      text = it.size.toString(),
                      fontSize = FontSize.REGULAR,
                      fontWeight = FontWeight.Medium
                    )
                  }
                  Text(
                    modifier = Modifier.weight(1f),
                    text = it.stock.toString(),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          } else {
            AlertTextField(
              modifier = Modifier.fillMaxWidth(),
              text = stringResource(Res.string.size),
              onClick = {
                showSizeDialog = true
              }
            )
          }
          CustomTextField(
            value = screenState.color,
            onValueChange = { viewModel.updateColor(it) },
            placeholder = stringResource(Res.string.color),
          )
          CustomTextField(
            value = "${if (screenState.price == 0.0) "" else screenState.price}",
            onValueChange = { viewModel.updatePrice(it.toDoubleOrNull()?: 0.0) },
            placeholder = stringResource(Res.string.price),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Number
            )
          )
        }
        PrimaryButton(
          text = if (id == null) stringResource(Res.string.add_new_product)
          else stringResource(Res.string.edit_product), icon = if (id == null) Resources.Icon.Plus
          else Resources.Icon.Checkmark, enabled = isFormValid, onClick = {
            viewModel.createProduct(onSuccess = {
              messageBarState.addSuccess(successAddProduct)
            }, onError = {
              messageBarState.addError(it)
            })
          })
      }
    }
  }
}
