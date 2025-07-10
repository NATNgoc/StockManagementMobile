package com.example.keygencetestapp.pages.stockInPage.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.UnitTypes
import com.example.keygencetestapp.ui.theme.KeyGenceTestAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.pages.stockInPage.constant.DefaultPurchaseOrder
import com.example.keygencetestapp.pages.stockInPage.viewModel.StockInViewModel
import com.example.keygencetestapp.utils.ResponseState

val MinusCircle: ImageVector
    get() {
        if (_MinusCircle != null) return _MinusCircle!!

        _MinusCircle = ImageVector.Builder(
            name = "MinusCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 12f)
                horizontalLineTo(9f)
                moveTo(21f, 12f)
                curveTo(21f, 16.9706f, 16.9706f, 21f, 12f, 21f)
                curveTo(7.02944f, 21f, 3f, 16.9706f, 3f, 12f)
                curveTo(3f, 7.02944f, 7.02944f, 3f, 12f, 3f)
                curveTo(16.9706f, 3f, 21f, 7.02944f, 21f, 12f)
                close()
            }
        }.build()

        return _MinusCircle!!
    }

private var _MinusCircle: ImageVector? = null


val PlusCircle: ImageVector
    get() {
        if (_PlusCircle != null) return _PlusCircle!!

        _PlusCircle = ImageVector.Builder(
            name = "PlusCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 9f)
                verticalLineTo(15f)
                moveTo(15f, 12f)
                horizontalLineTo(9f)
                moveTo(21f, 12f)
                curveTo(21f, 16.9706f, 16.9706f, 21f, 12f, 21f)
                curveTo(7.02944f, 21f, 3f, 16.9706f, 3f, 12f)
                curveTo(3f, 7.02944f, 7.02944f, 3f, 12f, 3f)
                curveTo(16.9706f, 3f, 21f, 7.02944f, 21f, 12f)
                close()
            }
        }.build()

        return _PlusCircle!!
    }

private var _PlusCircle: ImageVector? = null


@Composable
fun StockInItem(
    product: Product,
    modifier: Modifier = Modifier,
    navController: NavController,
    stockInViewModel: StockInViewModel = hiltViewModel()
) {

    var quantity by rememberSaveable { mutableIntStateOf(0) }
    val stockInState by stockInViewModel.stockInState.collectAsState()

    when (stockInState) {

        is ResponseState.Success -> {
            Toast.makeText(LocalContext.current,"Stock in successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.STOCK_IN_SCREEN) {
                popUpTo(Routes.HOME_SCREEN) {
                    inclusive = false
                }
            }
        }

        else -> {}
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Item Code:", product.itemCode)
                StockRow(product.stock, quantity, product.unit)
                InfoRow("Created At:", product.createdAt.toFormattedDateString())
                InfoRow("PO Number:", DefaultPurchaseOrder.ID)
                product.updatedAt?.let {
                    InfoRow("Updated At:", it.toFormattedDateString())
                }
                Spacer(modifier = Modifier.height(16.dp))
                QuantitySelector(
                    quantity = quantity,
                    onIncrement = { quantity = quantity + 1 },
                    onDecrement = { if (quantity > 0) quantity = quantity - 1 },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    onQuantityChange = {
                        quantity = it
                    }
                )
            }

        }
        StockInConfirm(product = product, quantity = quantity)
    }
}


@Composable
fun StockRow(
    curQuantity: Int,
    newQuantity: Int,
    unit: String,
    modifier: Modifier = Modifier
) {
    Row {
        Text(
            text = "Stock:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$curQuantity",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (newQuantity > 0) "+ $newQuantity" else "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0E700E) // Dark Green
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = unit,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

private fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    stockInViewModel: StockInViewModel = hiltViewModel()
) {
    var textValue by rememberSaveable { mutableStateOf(quantity.toString()) }
    val stockInState by stockInViewModel.stockInState.collectAsState()
    val isEnabled = stockInState !is ResponseState.Loading

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
                .clickable(enabled = isEnabled) {
                        onDecrement()
                        textValue = (textValue.toIntOrNull()?.minus(1) ?: 0).coerceAtLeast(0)
                            .toString()
                },
            tint = MaterialTheme.colorScheme.primary
        )
        TextField(
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    textValue = "0"
                    onQuantityChange(0)
                } else if (newValue.all { it.isDigit() }) {
                    val numValue = newValue.toLongOrNull()
                    if (numValue != null && numValue < Int.MAX_VALUE) {
                        textValue = newValue
                        onQuantityChange(numValue.toInt())
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
            enabled = isEnabled,
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
                .clickable(enabled = isEnabled) {
                        onIncrement()
                        textValue = (textValue.toIntOrNull()?.plus(1) ?: 1).toString()
                },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun StockInConfirm(
    product: Product,
    quantity: Int,
    modifier: Modifier = Modifier,
    stockInViewModel: StockInViewModel = hiltViewModel()
) {
    val stockInState by stockInViewModel.stockInState.collectAsState()
    Surface()
    {
        Button(
            onClick = {
                stockInViewModel.stockInItem(product, quantity = quantity)
            },
            enabled = stockInState !is ResponseState.Loading
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (stockInState is ResponseState.Loading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                }
                Text(text = "Stock In")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    KeyGenceTestAppTheme {
        val sampleProduct = Product(
            name = "Sample Product",
            itemCode = "SP-001",
            stock = 100,
            unit = UnitTypes.PIECE.code,
            createdAt = System.currentTimeMillis() - 86400000, // Yesterday
            updatedAt = System.currentTimeMillis()
        )
//        StockInItem(product = sampleProduct)
    }
}