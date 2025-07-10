package com.example.keygencetestapp.pages.OCRPage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.keygencetestapp.CreateNewItemScreen
import com.example.keygencetestapp.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import java.io.File

@Composable
fun CameraScreen(navController: NavController) {
    EnhancedCameraScreen(navController)
}

@Composable
fun EnhancedCameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var showBlur by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    var analyzedText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isLoadingResult by remember { mutableStateOf(false) }
    var previewSize by remember { mutableStateOf(IntSize.Zero) }
    LaunchedEffect(Unit) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    if (isCapturing) {
        Dialog(onDismissRequest = {
            isCapturing = false
        }) {
            Card(Modifier) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        if (!isLoadingResult) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = "Result",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                OutlinedTextField(
                                    value = analyzedText,
                                    onValueChange = { analyzedText = it },
                                    modifier = Modifier.wrapContentSize(),
                                    keyboardActions = KeyboardActions(onDone = { /* Handle Done action if needed */ }),
                                )
                            }
                        } else {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                isCapturing = false
                                analyzedText = ""
                            },
                            shape = RoundedCornerShape(10.dp),
                            enabled = analyzedText.isNotEmpty()
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                navController.navigate(CreateNewItemScreen(analyzedText))
                            },
                            shape = RoundedCornerShape(10.dp),
                            enabled = analyzedText.isNotEmpty()
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                "Smart Camera", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        controller = cameraController
                    }
                },
                modifier = Modifier.fillMaxSize().onGloballyPositioned {
                    previewSize = it.size
                }
            )


            if (showBlur) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.5f))
                        .blur(30.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(3f)
                    .border(
                        width = 3.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
//            Box(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .fillMaxWidth(0.7f)
//                    .aspectRatio(3f)
//
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.uo_right),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.TopEnd).size(24.dp),
//
//                    )
//                Icon(
//                    painter = painterResource(R.drawable.up_left),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.TopStart).size(24.dp),
//
//                    )
//
//                Icon(
//                    painter = painterResource(R.drawable.down_right),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.BottomEnd).size(24.dp),
//
//                    )
//                Icon(
//                    painter = painterResource(R.drawable.down_left),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.BottomStart).size(24.dp),
//
//                    )
//
//            }
            if (!isCapturing) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (previewSize.width <= 0 || previewSize.height <= 0) {
                                Toast.makeText(context, "Camera chưa sẵn sàng, vui lòng đợi...", Toast.LENGTH_SHORT).show()
                                return@IconButton
                            }
                            showBlur = true
                            isCapturing = true
                            isLoadingResult = true
                            captureAndRecognizeImage(
                                cameraController,
                                context,
                                previewSize,
                                onFinish = {
                                    showBlur = false
                                    analyzedText = it ?: ""
                                    isLoadingResult = false
                                },
                                resetLoadingStatus = {}
                            )
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .focusRequester(focusRequester) // Gán FocusRequester để yêu cầu focus
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Enter) {
                                    showBlur = true
                                    isCapturing = true
                                    captureAndRecognizeImage(
                                        cameraController,
                                        context,
                                        previewSize,
                                        onFinish = {
                                            showBlur = false
                                            analyzedText = if (it?.isNotEmpty() == true) it else "Not found"
                                        },
                                        resetLoadingStatus = {}
                                    )
                                    true
                                } else {
                                    false
                                }
                            },
                        ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.photo),
                            contentDescription = "Chụp ảnh",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp),

                            )
                    }
                }
            }

        }
    }
}

private fun captureAndRecognizeImage(
    controller: LifecycleCameraController,
    context: Context,
    previewSize: IntSize,
    onFinish: (String?) -> Unit,
    resetLoadingStatus: () -> Unit
) {
    // Kiểm tra previewSize hợp lệ
    if (previewSize.width <= 0 || previewSize.height <= 0) {
        Toast.makeText(context, "Lỗi: Kích thước preview không hợp lệ", Toast.LENGTH_SHORT).show()
        resetLoadingStatus()
        onFinish(null)
        return
    }

    val executor = ContextCompat.getMainExecutor(context)
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
        File.createTempFile("capture", ".jpg", context.cacheDir)
    ).build()

    controller.takePicture(
        outputFileOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(output.savedUri?.path)
                bitmap?.let { originalBitmap ->
                    // Tính toán vùng crop
                    val cropWidthPreview = previewSize.width * 0.7f
                    val cropHeightPreview = cropWidthPreview / 3f
                    val cropLeftPreview = (previewSize.width - cropWidthPreview) / 2
                    val cropTopPreview = (previewSize.height - cropHeightPreview) / 2

                    // Tính tỉ lệ scale
                    val scaleX = originalBitmap.width.toFloat() / previewSize.width
                    val scaleY = originalBitmap.height.toFloat() / previewSize.height

                    // Tính toán vùng crop trên bitmap gốc
                    val cropLeftBitmap = (cropLeftPreview * scaleX).toInt()
                    val cropTopBitmap = (cropTopPreview * scaleY).toInt()
                    val cropWidthBitmap = (cropWidthPreview * scaleX).toInt()
                    val cropHeightBitmap = (cropHeightPreview * scaleY).toInt()

                    // KIỂM TRA GIÁ TRỊ HỢP LỆ TRƯỚC KHI CROP
                    if (cropWidthBitmap <= 0 || cropHeightBitmap <= 0 ||
                        cropLeftBitmap < 0 || cropTopBitmap < 0 ||
                        cropLeftBitmap + cropWidthBitmap > originalBitmap.width ||
                        cropTopBitmap + cropHeightBitmap > originalBitmap.height
                    ) {
                        // Fallback: Sử dụng toàn bộ ảnh nếu không hợp lệ
                        recognizeTextFromBitmap(context, originalBitmap) { text ->
                            Toast.makeText(context, "Sử dụng toàn bộ ảnh do crop không hợp lệ", Toast.LENGTH_LONG).show()
                            resetLoadingStatus()
                            onFinish(text)
                        }
                        return
                    }

                    // Crop ảnh
                    val cropped = Bitmap.createBitmap(
                        originalBitmap,
                        cropLeftBitmap,
                        cropTopBitmap,
                        cropWidthBitmap,
                        cropHeightBitmap
                    )

                    recognizeTextFromBitmap(context, cropped) { text ->
                        Toast.makeText(context, "Nhận diện: $text", Toast.LENGTH_LONG).show()
                        resetLoadingStatus()
                        onFinish(text)
                    }
                } ?: run {
                    resetLoadingStatus()
                    onFinish(null)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Lỗi chụp ảnh: ${exception.message}", Toast.LENGTH_SHORT).show()
                resetLoadingStatus()
                onFinish(null)
            }
        }
    )
}



//private fun captureAndRecognizeImage(
//    controller: LifecycleCameraController,
//    context: Context,
//    onFinish: (String?) -> Unit,
//    resetLoadingStatus: () -> Unit
//) {
//    val executor = ContextCompat.getMainExecutor(context)
//    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
//        File.createTempFile("capture", ".jpg", context.cacheDir)
//    ).build()
//
//    controller.takePicture(
//        outputFileOptions,
//        executor,
//        object : ImageCapture.OnImageSavedCallback {
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                val bitmap = BitmapFactory.decodeFile(output.savedUri?.path)
//                bitmap?.let {
//                    // Crop chính xác vùng khung trắng (giả sử khung trắng là 70% width, tỉ lệ 3:1, nằm giữa)
//                    val cropWidth = (it.width * 0.7).toInt()
//                    val cropHeight = (cropWidth / 3f).toInt()
//                    val x = (it.width - cropWidth) / 2
//                    val y = (it.height - cropHeight) / 2
//
//                    val cropped = Bitmap.createBitmap(
//                        it, x, y, cropWidth, cropHeight
//                    )
//
//                    recognizeTextFromBitmap(context, cropped) { text ->
//                        Toast.makeText(context, "Nhận diện: $text", Toast.LENGTH_LONG).show()
//                        resetLoadingStatus()
//                        onFinish(text)
//                    }
//                } ?: onFinish(null)
//            }
//
//            override fun onError(exception: ImageCaptureException) {
//                Toast.makeText(context, "Lỗi chụp ảnh: ${exception.message}", Toast.LENGTH_SHORT)
//                    .show()
//                resetLoadingStatus()
//                onFinish(null)
//            }
//        }
//    )
//}

// Hàm nhận diện text trên bitmap vừa crop
private fun recognizeTextFromBitmap(
    context: Context,
    bitmap: Bitmap,
    onResult: (String) -> Unit
) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            onResult(visionText.text)
        }
        .addOnFailureListener { e ->
            onResult("Không nhận diện được: ${e.message}")
        }
}
