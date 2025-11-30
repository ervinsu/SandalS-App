import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
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
import com.shobaaa.id.shared.TextWhite
import com.shobaaa.id.shared.component.AlertTextField
import com.shobaaa.id.shared.component.CustomTextField
import com.shobaaa.id.shared.component.PrimaryButton
import com.shobaaa.id.shared.component.dialog.CategoriesDialog
import com.shobaaa.id.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sandals.shared.generated.resources.Res
import sandals.shared.generated.resources.add_new_product
import sandals.shared.generated.resources.edit_product
import sandals.shared.generated.resources.new_product

@OptIn(ExperimentalMaterial3Api::class) @Composable
fun ManageProductScreen(
  id: String?,
  navigateBack: () -> Unit
) {

  val messageBarState = rememberMessageBarState()
  var category by remember { mutableStateOf(ProductCategory.Shoes) }
  var showCategoriesDialog by remember { mutableStateOf(false) }

  AnimatedVisibility(
    visible = showCategoriesDialog
  ) {
    CategoriesDialog(
      category = category,
      onDismiss = {
        showCategoriesDialog = false
      },
      onConfirmClick = {
        category = it
        showCategoriesDialog = false
      }
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
            .verticalScroll(rememberScrollState()),
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
              .clickable(

              ) {
                println("Triggered!")
              },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              modifier = Modifier.size(24.dp),
              painter = painterResource(Resources.Icon.Plus),
              contentDescription = "Plus Icon",
              tint = IconPrimary
            )
          }
          CustomTextField(
            value = "",
            onValueChange = { },
            placeholder = "Title"
          )
          CustomTextField(
            modifier = Modifier.height(168.dp),
            value = "",
            onValueChange = { },
            placeholder = "Description",
            expanded = true
          )
          AlertTextField(
            modifier = Modifier.fillMaxWidth(),
            text = category.title,
            onClick = {
              showCategoriesDialog = true
            }
          )
          CustomTextField(
            modifier = Modifier.height(168.dp),
            value = "",
            onValueChange = { },
            placeholder = "Description",
            expanded = true
          )
        }
        PrimaryButton(
          text = if (id == null) stringResource(Res.string.add_new_product)
          else stringResource(Res.string.edit_product), icon = if (id == null) Resources.Icon.Plus
          else Resources.Icon.Checkmark, enabled = true, onClick = {})
      }
    }
  }
}
