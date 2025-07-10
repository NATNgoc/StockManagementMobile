package com.example.keygencetestapp.pages.stockOutPage.component.stockOutAction

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keygencetestapp.R
import com.example.keygencetestapp.components.PlaceHolderScreen
import com.example.keygencetestapp.components.PortableAlertDialog
import com.example.keygencetestapp.components.PortableConfirmDialog
import com.example.keygencetestapp.components.PortableLoading
import com.example.keygencetestapp.components.ShadowCard
import com.example.keygencetestapp.constant.Routes

import com.example.keygencetestapp.pages.stockOutPage.type.ItemStockOutDetailProgress
import com.example.keygencetestapp.pages.stockOutPage.type.StockOutActionProgress
import com.example.keygencetestapp.pages.stockOutPage.viewModel.PODetailViewModel
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.TablerIcons

import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.MoodCry
import compose.icons.tablericons.MoodHappy
import compose.icons.tablericons.Qrcode


@Composable
fun StockOutActionScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: PODetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val po = viewModel.poDetailState.collectAsState().value
    var code by remember { mutableStateOf("") }
    var selectedItem by remember { mutableIntStateOf(0) }
    //    -----------------------------------------------------------
    val stockOutProgress = viewModel.stockOutProgress.collectAsState().value
    var isSuccessScan by remember { mutableStateOf(true) }
    val isFinishedScan = viewModel.isFinisedScan.collectAsState()
    val resultScanState = viewModel.resultScan.collectAsState()
    val stockOutState = viewModel.stockOutState.collectAsState()
//    -----------------------------------------------------------

    LaunchedEffect(Unit) {
        viewModel.getPOById(id)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            StockOutActionNavigationBar(selectedItem) {
                selectedItem = it
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (!isSuccessScan && resultScanState.value is ResponseState.Error<*>) {
                PortableConfirmDialog(
                    {
                        isSuccessScan = !isSuccessScan
                        viewModel.resetScanState()
                    },
                    {
                        isSuccessScan = !isSuccessScan
                        viewModel.resetScanState()
                    },
                    "Error",
                    (resultScanState.value as ResponseState.Error<*>).message,
                    icon = TablerIcons.MoodCry
                )
            }



            when (val state = po) {
                is ResponseState.Loading -> PortableLoading()
                is ResponseState.Success -> {
                    if (isFinishedScan.value) {
                        PortableConfirmDialog(
                            {
                                viewModel.resetFinished()
                            },
                            {
                                viewModel.resetFinished()
                            },
                            "Finished",
                            "Check finished! Let's click the button \"Finished\"",
                            icon = TablerIcons.MoodHappy
                        )
                    }
                    state.data?.let { _ ->
                        when (selectedItem) {
                            0 -> {
                                GeneralScreen(
                                    Modifier.fillMaxSize(), code, stockOutProgress, {
                                        code = it
                                        if (code.length >= 20) {
                                            viewModel.scanItemId(code.trim())
                                            if (resultScanState.value is ResponseState.Success) {
                                                isSuccessScan = true
                                            } else {
                                                isSuccessScan = false
                                            }
                                            code = ""
                                        }
                                    }, {
                                        viewModel.stockOutProduct()
                                    },

                                    stockOutState.value
                                ) {
                                    navController.navigate(Routes.STOCK_OUT_SCREEN)
                                }
                            }

                            1 -> {
                                ProductScanList(
                                    stockOutProgress.listItemProgress,
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                is ResponseState.Error -> PlaceHolderScreen()
                else ->
                    PlaceHolderScreen()
            }
        }
    }

}


@Composable
fun QrCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    onQrClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val controller = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    ShadowCard(
        modifier
            .fillMaxWidth()
            .border(
                width = 1.dp, brush = SolidColor(Color.LightGray),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Scan QR",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    placeholder = { Text("Scan or input the code") },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                controller?.hide()
                            }
                        },
                    singleLine = true,
                    shape = RoundedCornerShape(6.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.None,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            controller?.hide()

                        }
                    )
                )
                IconButton(
                    onClick = onQrClick,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .background(Color(0xFFBDBDBD), shape = RoundedCornerShape(6.dp))
                ) {
                    Icon(
                        imageVector = TablerIcons.Qrcode,
                        contentDescription = "Quét mã QR",
                        tint = Color.DarkGray
                    )
                }
            }
        }
    }

}

@Composable
fun CheckStockOutState(
    isShowDialog: Boolean,
    onFail: () -> Unit,
    onSuccess: () -> Unit,
    stockOutState: ResponseState<Boolean>,
) {

    Box(Modifier.fillMaxSize().background(Color.Transparent)) {
        when (stockOutState) {
            is ResponseState.Loading -> {
                PortableLoading(Modifier.align(Alignment.Center))
            }

            is ResponseState.Error<*> -> {
                val errorState = stockOutState as ResponseState.Error<Boolean>
                if (isShowDialog) {
                    PortableConfirmDialog(
                        icon = TablerIcons.MoodCry,
                        onDismissRequest = {
                            onFail()
                        },
                        onConfirmation = {
                            onFail()
                        },
                        dialogContent = {
                        },
                        dialogText = errorState.message,
                        dialogTitle = "Error Stock out"
                    )
                }
            }

            is ResponseState.Success<*> -> {
                if (isShowDialog) {
                    PortableConfirmDialog(
                        icon = TablerIcons.MoodHappy,
                        onDismissRequest = {
                            onSuccess()
                        },
                        onConfirmation = {
                            onSuccess()
                        },
                        dialogContent = {

                        },
                        dialogText = "Navigate to PO Screen",
                        dialogTitle = "Success"
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun GeneralScreen(
    modifier: Modifier,
    code: String,
    stockOutProgress: StockOutActionProgress,
    onCodeChange: (String) -> Unit = {},
    onStockOut: () -> Unit = {},
    stockOutState: ResponseState<Boolean>,
    onNavigateToPODashboard: () -> Unit = {},
) {
    var isShowStockOutDialog by remember { mutableStateOf(false) }
    LaunchedEffect(stockOutState) {
        if (stockOutState !is ResponseState.Idle) {
            isShowStockOutDialog = true
        }
    }
    if (stockOutState !is ResponseState.Idle<*>) {
        CheckStockOutState(isShowStockOutDialog, {
            isShowStockOutDialog = false
        }, {
            onNavigateToPODashboard()
        },
            stockOutState
        )
    }

    Column(
        modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ProgressSection(stockOutProgress)
        Spacer(Modifier.height(16.dp))
        QrCodeInput(code, {
            onCodeChange(it)
        }, {
        })
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (stockOutProgress.remain == 0) {
                StockOutConfirm(onConfirm = {
                    onStockOut()
                })
            }
        }
    }
}

@Composable
fun StockOutConfirm(onConfirm: () -> Unit) {
    androidx.compose.material3.Button(
        onClick = {
            onConfirm()
        },
    ) {
        Column {
            Icon(
                imageVector = TablerIcons.ChevronRight,
                contentDescription = "Next"
            )
            Text("Stock out")
        }
    }
}

@Composable
fun ProductScanList(items: List<ItemStockOutDetailProgress>, modifier: Modifier = Modifier) {
    ShadowCard(
        modifier.border(
            width = 1.dp, brush = SolidColor(Color.LightGray),
            shape = RoundedCornerShape(10.dp)
        ),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black
        )
    ) {
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Text(
                "List Items",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {

                items(items) {
                    ProductScanItemCard(
                        name = it.name,
                        itemCode = it.itemCode,
                        required = it.required,
                        scanned = it.scanned
                    )
                }
            }
        }
    }
}

@Composable
fun ProductScanItemCard(
    name: String, itemCode: String, required: Int, scanned: Int, modifier: Modifier = Modifier
) {

    val remain = required - scanned
    val progress = if (required == 0) 0f else scanned.toFloat() / required
    val DoneBadge = @Composable {
        Badge(
            containerColor = colorResource(R.color.success_background),
            contentColor = colorResource(R.color.success_text)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_24dp_1f1f1f_fill0_wght600_grad0_opsz24),
                    null
                )
                Text("Done")
            }
        }
    }
    val UnDoneBadge = @Composable {
        Badge(
            containerColor = colorResource(R.color.pending_background),
            contentColor = colorResource(R.color.pending_text)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.bolt_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
                    null
                )
                Text("Not")
            }
        }
    }
    Card(
        modifier = modifier, shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        name,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        itemCode,
                        color = Color(0xFFB0B0B0),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (remain > 0) UnDoneBadge() else DoneBadge()
            }
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Required", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("$required", fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Scanned", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("$scanned", fontWeight = FontWeight.Bold, color = Color(0xFF007AFF))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Remain", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("$remain", fontWeight = FontWeight.Bold, color = Color(0xFFFF6600))
                }
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF007AFF),
                trackColor = Color(0xFFF5F5F5),
            )
        }
    }
}



@Composable
fun ProgressSection(stockOutProgress: StockOutActionProgress) {
    val progress =
        if (stockOutProgress.totalScanned == 0) 0f else stockOutProgress.totalScanned.toFloat() / stockOutProgress.required

    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        label = "progressAnimation",
        animationSpec = tween(durationMillis = 300)
    )
    ShadowCard(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp, brush = SolidColor(Color.LightGray),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.Black
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text(
                    "Stock out progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("${stockOutProgress.totalScanned}/${stockOutProgress.required}")
            }
            LinearProgressIndicator(
                progress = { animatedProgress.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ProgressIndicator(
                    "${stockOutProgress.totalScanned}",
                    "Completed",
                    color = Color(0xFF17B963)
                )
                ProgressIndicator("${stockOutProgress.remain}", "Remain", color = Color(0xFFFF6600))
                ProgressIndicator(
                    "${(progress * 100).toInt()}%",
                    "Progress",
                    color = Color(0xFF007AFF)
                )
            }
        }
    }
}

@Composable
fun ProgressIndicator(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            content
        )
    }
}



@Preview
@Composable
fun QRPreview(modifier: Modifier = Modifier) {
    var code by remember { mutableStateOf("") }

    Surface {
        QrCodeInput(
            value = code,
            onValueChange = { code = it },
            onQrClick = { /* Mở màn hình quét QR */ }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductScanItemCardPreview() {
    ProductScanItemCard(
        name = "Laptop Dell Inspiron 15",
        itemCode = "ITM001",
        required = 10,
        scanned = 0,
        modifier = Modifier.padding(8.dp)
    )
}


//@Preview(showBackground = true)
//@Composable
//fun StockOutScreenPreview() {
//    val sampleProducts = listOf(
//        ProductScanItem(
//            name = "Mainboard XYZ", itemCode = "MB-001", required = 3, scanned = 0
//        ), ProductScanItem(
//            name = "CPU Intel i7", itemCode = "CPU-002", required = 2, scanned = 0
//        )
//    )
//    val samplePo = ProcessOrder(
//        note = "Sample PO for preview",
//        type = ProcessOrderType.STOCK_OUT.name,
//        status = ProcessOrderStatus.NEW.name
//    )
//
//    StockOutScreen(
//        po = samplePo,
//        productList = sampleProducts,
//        onScanBarcode = { },
//        modifier = Modifier.fillMaxSize()
//    )
//}


