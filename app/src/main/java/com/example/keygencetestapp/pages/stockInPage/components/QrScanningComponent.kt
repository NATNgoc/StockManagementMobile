package com.example.keygencetestapp.pages.stockInPage.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.example.keygencetestapp.CreateNewItemScreen
import com.example.keygencetestapp.R
import com.example.keygencetestapp.components.PortableAlertDialog
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.pages.stockInPage.viewModel.POViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Battery1

@Composable
fun QrScanning(modifier: Modifier = Modifier, navController: NavController) {
    var item by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current
    var inputText by remember {
        mutableStateOf("")
    }
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    var isShowDialog by remember { mutableStateOf(false) }
//    CheckingItem(inputText = inputText, resetInputText = {
//        inputText = ""
//    },
//        navController = navController)
    if (isShowDialog) {
        PortableAlertDialog(
            onDismissRequest = {
                inputText = ""
                isShowDialog = false
            },
            onConfirmation = {
                navController.navigate(
                    CreateNewItemScreen(
                        id = inputText
                    )
                )
            },
            dialogTitle = "Item #-${inputText}",
            dialogText = "Confirm to stock in",
            icon = TablerIcons.Battery1
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(), // Quan trọng: Đảm bảo layout hợp lệ
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.qr_scanning)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "QR Scanning Animation",
            modifier = Modifier
                .scale(2.0f)
        )
        QRHiddenInput(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0f),
            inputText = inputText,
        ) {
            inputText = it
            isShowDialog = true
        }
    }

}

@Composable
fun QRHiddenInput(
    modifier: Modifier = Modifier,
    poViewModel: POViewModel = hiltViewModel(),
    inputText: String,
    onTextChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val controller = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) controller?.hide()
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusRequester.requestFocus()
            }
            .background(Color.Transparent)
            .onGloballyPositioned {
                    focusRequester.requestFocus()
                    controller?.hide()

            }
    ) {
        TextField(
            value = inputText,
            onValueChange = { newText ->
                onTextChange(newText)
            },
            modifier = Modifier
                .alpha(0f),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
    }
}