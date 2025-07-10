package com.example.keygencetestapp.pages.oneCheckScanPage

import ScanResult
import ScanViewModel
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keygencetestapp.R
import com.example.keygencetestapp.components.PortableAlertDialog
import com.example.keygencetestapp.components.CustomFloatingActionButton
import com.example.keygencetestapp.components.OptionFilter
import com.example.keygencetestapp.components.PortableConfirmDialog
import com.example.keygencetestapp.ui.theme.KeyGenceTestAppTheme
import com.example.keygencetestapp.pages.oneCheckScanPage.components.ExportCsvDialogComponent
import com.example.keygencetestapp.pages.oneCheckScanPage.viewModels.ExportCsvViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.AlertCircle
import compose.icons.tablericons.Check
import compose.icons.tablericons.ClipboardX
import compose.icons.tablericons.Message
import compose.icons.tablericons.Unlink
import kotlinx.coroutines.delay

@Composable
fun InputPage(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel = viewModel(),
    exportCsvViewModel: ExportCsvViewModel = viewModel()
) {
    var textInput by rememberSaveable { mutableStateOf("") }
    var isScanning: Boolean by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var isIgnoreErrorCard by rememberSaveable { mutableStateOf(true) }
    var isDisplayDialog by rememberSaveable { mutableStateOf(true) }
    var isShowErrorDialog by rememberSaveable { mutableStateOf(false) }
    var isShowCsvExportDialog by rememberSaveable { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        scanViewModel.initScanManagerAndNotification(context)
//    }

    if (isShowCsvExportDialog && scanViewModel.getList().isNotEmpty()) {
        ExportCsvDialogComponent(
            scanResultList = scanViewModel.getList(),
            exportCsvViewModel,
            onDismissRequest = {
                isShowCsvExportDialog = false
            },
            onConfirmation = {
                isShowCsvExportDialog = false
            })
    }

    // Dialog error
    if (isShowErrorDialog) {
        PortableConfirmDialog(
            onDismissRequest = { isShowErrorDialog = false },
            onConfirmation = { isShowErrorDialog = false },
            dialogTitle = "This package is not found",
            dialogText = "Confirm to rescan with the correct part",
            icon = TablerIcons.ClipboardX
        )
    }

    Scaffold(
        floatingActionButton = {
            CustomFloatingActionButton(
                onClick = {
                    if (scanViewModel.getList().isNotEmpty()) isShowCsvExportDialog = true
                    else Toast.makeText(context, "Chưa có dữ liệu xuất csv", LENGTH_SHORT).show()
                },
                icon = ImageVector.vectorResource(R.drawable.csv_icon),
                modifier = Modifier.padding(bottom = 20.dp)
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
//                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            FixatingInputField(
                isIgnoreSelected = isIgnoreErrorCard,
                isDisplayDialog = isDisplayDialog,
                value = textInput,
                onValueChange = {
                    textInput = it
                    isScanning = true
                },
                isToggleScanButton = isScanning,
                onScan = {
                    isScanning = !isScanning
                },
                modifier = Modifier.weight(1f),
                scanViewModel = scanViewModel,
                onResetButtonClick = {
                    isScanning = false
                    scanViewModel.clear()
                })

            if (isScanning) {
                Log.v("HiddenLog", "Đang thêm 1")
                HiddenInputField(onScan = { value ->
                    val isValid = value == textInput
                    Log.v("HiddenLog", "Đang thêm 2")
                    scanViewModel.addResult(value, isValid)
                    if (!isValid && isDisplayDialog) {
                        isShowErrorDialog = true
                    }
                })
            }

            OptionFilterList(
                isIgnoreSelected = isIgnoreErrorCard,
                isDialogSelected = isDisplayDialog,
                onIgnoreChange = {
                    isIgnoreErrorCard = !isIgnoreErrorCard
                },
                onDialogChange = {
                    isDisplayDialog = !isDisplayDialog
                })
        }
    }
}


@Composable
fun OptionFilterList(
    isIgnoreSelected: Boolean,
    isDialogSelected: Boolean,
    onIgnoreChange: () -> Unit,
    onDialogChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        OptionFilter(
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 10.dp),
            icon = TablerIcons.AlertCircle,
            label = "Ignore",
            onClick = {
                Log.d("OptionFilter", "Settings clicked")
                onIgnoreChange()
            },
            isSelected = isIgnoreSelected
        )
        OptionFilter(
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 10.dp),
            icon = TablerIcons.Message,
            label = "Dialog",
            onClick = {
                Log.d("OptionFilter", "Settings clicked")
                onDialogChange()
            },
            isSelected = isDialogSelected
        )
    }
}


@Preview
@Composable
fun OptionFilterPreview() {
    KeyGenceTestAppTheme {
        Surface {
//            Row {
//                OptionFilter(icon = TablerIcons.AlertCircle, label = "Ignore", onClick = {
//                    Log.d("OptionFilter", "Settings clicked")
//                })
//                OptionFilter(icon = TablerIcons.Message, label = "Dialog", onClick = {
//                    Log.d("OptionFilter", "Settings clicked")
//                })
//            }
        }
    }
}

@Composable
fun ScanResultScreen(
    isIgnoreErrorCard: Boolean,
    isDisplayDialog: Boolean,
    scanViewModel: ScanViewModel,
    modifier: Modifier = Modifier
) {
    val scanList by scanViewModel.scanResults.collectAsState()
    val listState = rememberLazyListState()
    val displayList = remember(scanList, isIgnoreErrorCard) {
        if (isIgnoreErrorCard) {
            scanList.filter { it.result }
        } else {
            scanList
        }
    }
    LaunchedEffect(scanList.size) {
        if (scanList.isNotEmpty()) {
//            if (!scanList.last().result) {
//                scanViewModel.triggerBuzzer()
//            }
            listState.animateScrollToItem(scanList.size - 1)
        }
    }
    LazyColumn(
        reverseLayout = true, modifier = modifier, verticalArrangement = Arrangement.spacedBy(
            10.dp
        ), state = listState
    ) {
        items(items = displayList) { item ->
            ResultCard(
                scanResult = item, modifier = Modifier.animateItem()
            )

        }
    }
}

@Composable
fun ResultCard(
    scanResult: ScanResult, modifier: Modifier = Modifier
) {

    Surface(modifier.background(Color.Transparent)) {
        ElevatedCard(
            modifier = Modifier.height(50.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "#" + scanResult.id.toString(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = scanResult.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    modifier = Modifier.weight(1f),
                    fontStyle = FontStyle.Italic
                )
                if (scanResult.result) Icon(
                    imageVector = TablerIcons.Check,
                    contentDescription = "Mood",
                    Modifier.background(Color.Green)
                )
                else {
                    Icon(
                        imageVector = TablerIcons.Unlink,
                        contentDescription = "Mood",
                        Modifier.background(Color.Red)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun ResultCardPreview() {
    KeyGenceTestAppTheme {
        Surface {
            ResultCard(ScanResult(result = false, id = 1, value = "123123123"))
        }
    }
}

@Composable
fun FixatingInputField(
    value: String,
    onValueChange: (value: String) -> Unit,
    isToggleScanButton: Boolean,
    onScan: () -> Unit,
    onResetButtonClick: () -> Unit,
    scanViewModel: ScanViewModel,
    modifier: Modifier = Modifier,
    isIgnoreSelected: Boolean,
    isDisplayDialog: Boolean,
) {
    var isLock by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val controller = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(150)
        controller?.hide()
    }
    Column(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = stringResource(R.string.Fixating_Title))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            OutlinedTextField(
                readOnly = isLock,
                value = value,
                onValueChange = {
                    onValueChange(it)
                    isLock = true

                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFF5F5F5)
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.Fixating_Field_PlaceHolder),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                            controller?.hide()
                    },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    })
            )



            Button(
                onClick = {
                    isLock = !isLock
                    if (!isLock) {
                        // khi Unlock, focus vào field và mở bàn phím
                        focusManager.moveFocus(FocusDirection.Enter)
                    } else {
                        // khi Lock, clear focus
                        focusManager.clearFocus()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLock) Color.Gray else MaterialTheme.colorScheme.primary
                ),
            ) {
                Icon(
                    painter = if (isLock) painterResource(R.drawable.lock) else painterResource(R.drawable.lock_open),
                    contentDescription = if (isLock) "Selected icon button" else "Unselected icon button."
                )
            }
        }
        ScanResultScreen(
            isIgnoreSelected, isDisplayDialog, scanViewModel, modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = {
                isLock = false
                onValueChange("")
                focusManager.moveFocus(FocusDirection.Enter)
                focusRequester.requestFocus()
                onResetButtonClick()
            }) {
                Text(text = "Đặt lại")
            }
            if (value.isNotEmpty() && isLock) {
                Button(onClick = { onScan() }) {
                    Text(text = if (isToggleScanButton) "Stop Scan" else "Scan")
                }
            }
        }
    }
}

@Composable
fun HiddenInputField(modifier: Modifier = Modifier, onScan: (String) -> Unit) {
    var inputText by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val controller = LocalSoftwareKeyboardController.current
    var lastIndex by rememberSaveable { mutableStateOf(-1) }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(150)
        controller?.hide()
    }

    Surface(modifier = modifier) {
        Column(modifier = Modifier) {
            TextField(
                value = inputText,
                onValueChange = { newText ->
                    Log.v("HiddenLog", "Đang thay đổi giá trị $newText ${newText.lastIndex}")
                    inputText = newText.trim()
                    val textCompare = inputText.substring(lastIndex+1, newText.length)
                    Log.v("HiddenLog", "Đang so sanh $textCompare")
                    if (newText.isNotEmpty()) {
                        onScan(textCompare)
                        lastIndex = inputText.lastIndex
//                        inputText=""
                    }
                },
                modifier = Modifier
                    .size(1.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            controller?.hide()
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
            )
        }
    }
}

@Preview
@Composable
fun FixatingInputFieldPreview() {
    KeyGenceTestAppTheme {
        Surface {
//            FixatingInputField()
        }
    }
}
