package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.pages.stockInPage.components.MinusCircle
import com.example.keygencetestapp.pages.stockInPage.components.PlusCircle
import com.example.keygencetestapp.pages.stockOutPage.viewModel.SearchAndChooseItemViewModel


@Composable
fun StepChooseCountAndConfirm(
    modifier: Modifier = Modifier,
    createItemViewModel: SearchAndChooseItemViewModel = hiltViewModel()
) {
    var quantity by rememberSaveable { mutableIntStateOf(0) }
    var selectedProduct = createItemViewModel.selectedProduct.collectAsState()
    Column(
        modifier.scrollable(
            rememberScrollState(),
            orientation = Orientation.Vertical
        ), verticalArrangement = Arrangement.spacedBy(
            5.dp, alignment = Alignment.Top
        )
    ) {
        NumberStep(2, "Choose Quantity Of This Item")
        if (selectedProduct.value != null) {
            SmallItemCard(product = selectedProduct.value!!, isSelected = false, onClick = {})
        }
        StockOutQuantitySelector(
            quantity = quantity,
            onIncrement = {
                quantity += 1
                createItemViewModel.setNumberStockOut(quantity)
            },
            onDecrement = {
                if (quantity > 0) {
                    quantity -= 1
                    createItemViewModel.setNumberStockOut(quantity)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onQuantityChange = {
                quantity = it
                createItemViewModel.setNumberStockOut(quantity)
            },
            limit = selectedProduct.value!!.stock
        )
        if (quantity > selectedProduct.value!!.stock) {
            Text(
                text = "Warning: Stock out quantity (${quantity}) is greater than current stock (${selectedProduct.value!!.stock}).",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()

            )
        }
    }
}
// New, ongoing, finished
@Composable
fun StockOutQuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    limit: Int = 0
) {
    var textValue by rememberSaveable { mutableStateOf(quantity.toString()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = MinusCircle,
            contentDescription = "Decrement Quantity",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    val temp = textValue.toIntOrNull()
                    if (temp != null && temp > 0 && temp <= limit) {
                        onDecrement()
                        textValue = (temp.minus(1)).coerceAtLeast(0)
                            .toString()
                    }
                },
            tint = MaterialTheme.colorScheme.primary
        )
        TextField(
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    textValue = "0"
                    onQuantityChange(0)
                } else if (newValue.all { it.isDigit() } && newValue.toInt() <= limit) {
                    val numValue = newValue.toInt()
                    if (numValue < Int.MAX_VALUE) {
                        textValue = newValue
                        onQuantityChange(numValue)
                    }
                }
            },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .widthIn(min = 50.dp, max = 150.dp),
//            enabled = isEnabled,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        Icon(
            imageVector = PlusCircle,
            contentDescription = "Increment Quantity",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    val temp = textValue.toIntOrNull()
                    if (temp != null && temp >= 0 && temp < limit) { // Optimized condition
                        onIncrement()
                        textValue = temp.plus(1).toString()
                    }
                },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}