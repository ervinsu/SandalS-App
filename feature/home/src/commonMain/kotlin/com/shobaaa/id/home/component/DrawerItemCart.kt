package com.shobaaa.id.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shobaaa.id.home.domain.DrawerItem
import com.shobaaa.id.shared.FontSize
import com.shobaaa.id.shared.IconPrimary
import com.shobaaa.id.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sandals.shared.generated.resources.Res
import sandals.shared.generated.resources.blog
import sandals.shared.generated.resources.contact_us
import sandals.shared.generated.resources.location
import sandals.shared.generated.resources.profile
import sandals.shared.generated.resources.sign_out

@Composable
fun DrawerItemCard(
  drawerItem: DrawerItem,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(size = 99.dp))
      .clickable { onClick() }
      .padding(
        vertical = 12.dp,
        horizontal = 12.dp
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(drawerItem.icon),
      contentDescription = "Drawer item icon",
      tint = IconPrimary
    )
    Spacer(modifier = Modifier.width(12.dp))
    Text(
      text = getTitleText(drawerItem.title),
      color = TextPrimary,
      fontSize = FontSize.EXTRA_REGULAR
    )
  }
}

@Composable
private fun getTitleText(text: String): String {
  return when (text) {
    "Profile" -> stringResource(Res.string.profile)
    "Blog" -> stringResource(Res.string.blog)
    "Location" -> stringResource(Res.string.location)
    "Contact Us" -> stringResource(Res.string.contact_us)
    "Sign out" -> stringResource(Res.string.sign_out)
    else -> text
  }
}
