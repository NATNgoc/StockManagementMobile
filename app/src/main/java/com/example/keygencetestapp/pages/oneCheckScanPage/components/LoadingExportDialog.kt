package com.example.keygencetestapp.pages.oneCheckScanPage.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.example.keygencetestapp.R

@Composable
fun LoadingExportDialog(
    onDismissDialog: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    androidx.compose.ui.window.Dialog(onDismissDialog) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.csv_loading)
                .build(),
            imageLoader = imageLoader,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }

}

@Preview
@Composable
fun LoadingExportDialogPreview() {
    LoadingExportDialog()
}