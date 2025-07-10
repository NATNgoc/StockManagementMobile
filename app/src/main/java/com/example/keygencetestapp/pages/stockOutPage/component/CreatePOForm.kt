package com.example.keygencetestapp.pages.stockOutPage.component

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.components.DatePickerModal
import com.example.keygencetestapp.components.FormRow
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.pages.stockOutPage.viewModel.CreatePOViewModel
import com.example.keygencetestapp.utils.convertMillisToDate
import compose.icons.TablerIcons
import java.text.SimpleDateFormat
import java.util.Calendar
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.Note
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.max
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.keygencetestapp.PODetailScreen
import com.example.keygencetestapp.StockOutScreen
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.components.ShadowCard
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.pages.stockOutPage.StockOutTabs
import com.example.keygencetestapp.pages.stockOutPage.viewModel.ProductWithStockOutCount
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.tablericons.ArrowDown
import compose.icons.tablericons.ArrowsDown
import compose.icons.tablericons.ChevronRight

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("FrequentlyChangingValue")
@Composable
fun CreateStockOutPOForm(
    navController: NavController,
    modifier: Modifier = Modifier,
    createPOViewModel: CreatePOViewModel = hiltViewModel(),
) {
    var note by remember { mutableStateOf("") }
    val status = ProcessOrderStatus.NEW.name
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val stockedOutProducts = createPOViewModel.selectedProduct.collectAsState().value
    val dateRangePickerState = rememberDateRangePickerState()
    //-------------------------------------------------------
    var isShowDatePicker by remember { mutableStateOf(false) }
    var isPlannedDateError by remember { mutableStateOf(false) }
    val plannedDateErrorMessage = "Plan date must be greater than or equal to current date"
    var isShowDialog by remember { mutableStateOf(false) }
    val onAddItemClick = {
        isShowDialog = true
    }
    val createPOState = createPOViewModel.createPOState.collectAsState().value

    if (isShowDialog) {
        AddItemDialog({
            isShowDialog = false
        }, {})
    }


    val listState = rememberLazyListState()


    Box(
        modifier
            .alpha(if (createPOState is ResponseState.Loading<*>) 0.5f else 1f)
            .clickable(
                enabled = createPOState !is ResponseState.Loading<*>,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )) {
        when (createPOState) {
            is ResponseState.Loading<*> -> {
                ContainedLoadingIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .zIndex(1F)
                )
            }

            is ResponseState.Success<*> -> {
                Toast.makeText(
                    LocalContext.current,
                    "Initialize Stock out PO successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(
                    StockOutScreen(
                        tab = StockOutTabs.MANAGE
                    )
                ) {
                    popUpTo(Routes.HOME_SCREEN) {
                        inclusive = false
                    }
                }
            }

            is ResponseState.Error<*> -> {
                Toast.makeText(LocalContext.current, createPOState.message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {

            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)

        ) {
            ShadowCard(
                modifier = Modifier.shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                ),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Gray
                )

            ) {
                Column(Modifier.padding(10.dp)) {
                    PlanRangeDate(
                        startDate = startDate,
                        endDate = endDate,
                        isShowDatePicker = isShowDatePicker,
                        dateRangePickerState = dateRangePickerState,
                        onShowDatePickerChange = { isShowDatePicker = it },
                        onDateRangeSelected = { start, end ->
                            startDate = start
                            isPlannedDateError = !isPlanDateValid(start)
                            endDate = end
                        },
                        plannedDateErrorMessage = plannedDateErrorMessage,
                        isPlannedDateError = isPlannedDateError
                    )
                    Note(note) {
                        note = it
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 16.dp)) {
                items(stockedOutProducts) {
                    StockedProduct(item = it)
                }
            }
        }
        OverflowingHorizontalFloatingToolbar(
            listState, Modifier.align(Alignment.BottomCenter), onPlusClick = {
                if (!isPlannedDateError) {
                    onAddItemClick()
                }
            },
            onConfirm = {
                if (!isPlannedDateError && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                    createPOViewModel.createPO(
                        note,
                        dateRangePickerState.selectedStartDateMillis!!.toLong(),
                        dateRangePickerState.selectedEndDateMillis!!.toLong()
                    )
                }
            },
            isEnableSubmit = !isPlannedDateError && stockedOutProducts.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()
        )
    }
}

@Composable
private fun Note(note: String, onNoteChange: (it: String) -> Unit) {
    FormRow(title = "Note") {
        OutlinedTextField(
            value = note,
            onValueChange = { onNoteChange(it) },
            leadingIcon = { Icon(TablerIcons.Note, contentDescription = null) },
            label = { Text("Enter Note PO") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f
                )
            ),
            maxLines = 1,
            shape = MaterialTheme.shapes.medium
            )
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanRangeDate(
    modifier: Modifier = Modifier,
    plannedDateErrorMessage: String = "",
    isPlannedDateError: Boolean = false,
    startDate: String,
    endDate: String,
    isShowDatePicker: Boolean,
    dateRangePickerState: DateRangePickerState,
    onShowDatePickerChange: (Boolean) -> Unit,
    onDateRangeSelected: (start: String, end: String) -> Unit
) {
    val dateRangeText =
        if (startDate.isEmpty() || endDate.isEmpty()) "" else "$startDate → $endDate"
    val daysSelected = remember(startDate, endDate) {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            if (start != null && end != null) {
                val diff = ((end.time - start.time) / (1000 * 60 * 60 * 24)).toInt()
                if (diff >= 0) diff else 0
            } else 0
        } catch (e: Exception) {
            0
        }
    }

    FormRow(
        title = "Plan Date",
        modifier = modifier,
        isRequired = true,
        errorMessage = plannedDateErrorMessage,
        isError = isPlannedDateError
    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onShowDatePickerChange(true) }
//        ) {
//            OutlinedTextField(
//                value = dateRangeText,
//                onValueChange = {},
//                enabled = false,
////                readOnly = false,
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = {
//                    Text(
//                        "dd/MM/yyyy → dd/MM/yyyy",
//                        maxLines = 1,
//                        overflow = TextOverflow.Clip,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                },
//                leadingIcon = {
//                    IconButton(onClick = { onShowDatePickerChange(!isShowDatePicker) }) {
//                        Icon(
//                            imageVector = TablerIcons.Calendar,
//                            contentDescription = "Select date"
//                        )
//                    }
//                },
//                trailingIcon = {
//                    Icon(
//                        imageVector = TablerIcons.ChevronRight,
//                        contentDescription = null
//                    )
//                },
//                maxLines = 1,
//                textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
//            )
//        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowDatePickerChange(true) },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, Color.LightGray),
            tonalElevation = 0.dp,
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = TablerIcons.Calendar,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (dateRangeText.isEmpty()) "dd/MM/yyyy → dd/MM/yyyy" else dateRangeText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = TablerIcons.ChevronRight,
                    contentDescription = null
                )
            }
        }

        if (dateRangeText.isNotEmpty()) {
            Text(
                "$daysSelected days selected",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }

        if (isShowDatePicker) {
            DatePickerModal(
                onDateRangeSelected = { pair ->
                    val start = pair.first?.let { convertMillisToDate(it) } ?: ""
                    val end = pair.second?.let { convertMillisToDate(it) } ?: ""
                    onDateRangeSelected(start, end)
                    onShowDatePickerChange(false)
                },
                onDismiss = { onShowDatePickerChange(false) },
                modifier = Modifier,
                dateRangePickerState = dateRangePickerState
            )
        }
    }
}

@Composable
fun StockedProduct(modifier: Modifier = Modifier, item: ProductWithStockOutCount) {
    Surface(modifier) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(4.dp)
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.DarkGray,
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.product.itemCode,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(10.dp))
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                "Stock: ${item.product.stock}",
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                    Text("Name: " + item.product.name, style = MaterialTheme.typography.bodyLarge)


                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(TablerIcons.ArrowsDown, null, tint = Color.Red)
                    Text(
                        item.stockOutCount.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StockedProductPreview() {
    MaterialTheme {
        StockedProduct(
            item = ProductWithStockOutCount(
                product = Product(
                    itemCode = "ITEM001",
                    stock = 10,
                    name = "Sample Product",
                    id = "12312312312"
                ),
                stockOutCount = 5
            )
        )
    }
}


private fun isPlanDateValid(selectedDateString: String): Boolean {
    if (selectedDateString.isEmpty()) return true
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val selectedCalendar = Calendar.getInstance().apply {
        time = sdf.parse(selectedDateString)
    }
    val currentCalendar = Calendar.getInstance()
    currentCalendar.set(Calendar.HOUR_OF_DAY, 0); currentCalendar.set(
        Calendar.MINUTE,
        0
    ); currentCalendar.set(Calendar.SECOND, 0); currentCalendar.set(Calendar.MILLISECOND, 0)
    selectedCalendar.set(Calendar.HOUR_OF_DAY, 0); selectedCalendar.set(
        Calendar.MINUTE,
        0
    ); selectedCalendar.set(Calendar.SECOND, 0); selectedCalendar.set(Calendar.MILLISECOND, 0)
    return !selectedCalendar.before(currentCalendar)
}

@Preview
@Composable
fun PreviewCreateStockOutPO() {
    Surface {
//        CreateStockOutPOForm()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PlanRangeDatePreview() {
    MaterialTheme {
        var startDate by remember { mutableStateOf("10/07/2024") }
        var endDate by remember { mutableStateOf("12/07/2024") }
        var isShowDatePicker by remember { mutableStateOf(false) }
        val dateRangePickerState = rememberDateRangePickerState()

        PlanRangeDate(
            startDate = startDate,
            endDate = endDate,
            isShowDatePicker = isShowDatePicker,
            dateRangePickerState = dateRangePickerState,
            onShowDatePickerChange = { isShowDatePicker = it },
            onDateRangeSelected = { start, end ->
                startDate = start
                endDate = end
            })
    }
}