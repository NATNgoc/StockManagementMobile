package com.example.keygencetestapp.pages.stockInPage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.pages.stockInPage.constant.ImportOptionsConst
import com.example.keygencetestapp.pages.stockInPage.types.ImportOptionType


@Composable
fun ImportOptionList(modifier: Modifier = Modifier, navController: NavController) {
    Surface(modifier.background(Color.Transparent)) {
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ImportOption(Modifier.weight(1f), item = ImportOptionsConst.qrOption, onClick = {
                    navController.navigate(Routes.STOCK_IN_QR_CODE_SCAN)
                })
            }
            Row(Modifier.weight(1f)) {
                ImportOption(item = ImportOptionsConst.ocrOption, onClick = {
                    navController.navigate(Routes.STOCK_IN_OCR_SCAN)
                })
            }
        }
    }
}

@Preview
@Composable
fun ImportOptionListPreview() {
    Surface {
//        ImportOptionList()
    }
}

@Composable
fun ImportOption(modifier: Modifier = Modifier, item: ImportOptionType, onClick: () -> Unit = {}) {
    Surface(modifier
        .background(Color.White)
        .fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxSize(),
            onClick = { onClick() }) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .zIndex(1f)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = item.description,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Image(
                    bitmap = ImageBitmap.imageResource(item.iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .alpha(0.5f) // làm mờ 50%
                        .fillMaxSize(0.75f),
                    alignment = Alignment.TopEnd
                )
            }
        }
    }
}

@Preview
@Composable
fun ImportOptionPreview() {
    Surface() {
//        ImportOptionType()
    }
}