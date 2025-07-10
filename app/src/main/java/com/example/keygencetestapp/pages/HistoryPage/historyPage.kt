package com.example.keygencetestapp.pages.HistoryPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.R
import com.example.keygencetestapp.pages.HistoryPage.viewModel.HistoryViewModel
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryWithProduct
import com.example.keygencetestapp.pages.stockInPage.components.MinusCircle
import com.example.keygencetestapp.pages.stockInPage.components.PlusCircle
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.StockInHistory
import com.example.keygencetestapp.database.entities.StockType
import com.example.keygencetestapp.utils.ResponseState
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun HistoryPage(
    modifier: Modifier = Modifier,
    historyViewModel: HistoryViewModel = hiltViewModel() // Assuming you'll pass this or observe state from it
) {
    val stockHistory = historyViewModel.stockHistory.collectAsState()
    LaunchedEffect(Unit) {
        historyViewModel.getHistories()
    }
    when (val state = stockHistory.value) { // Extract the value here
        is ResponseState.Loading, is ResponseState.Idle -> { // Corrected line
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ResponseState.Success -> {
            // Assuming state.data is List<StockInHistory>
            HistoryList(historyItems = state.data, modifier = modifier)
        }
        is ResponseState.Error -> {
            Text("Error: ${state.message}", color = Color.Red)
        }
    }
}

@Composable
fun HistoryList(
    historyItems: List<HistoryWithProduct>,
    modifier: Modifier = Modifier
) {
    if (historyItems.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No history available.")
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(historyItems) { item ->
            HistoryItem(item = item)
        }
    }
}

@Composable
fun HistoryItem(
    item: HistoryWithProduct,
    modifier: Modifier = Modifier
) {
    val itemColor: Color
    var icon: ImageVector? = null // Made nullable
    val quantityPrefix: String

    when (item.history.type) {
        StockType.STOCK_IN.toString() -> {
            itemColor = Color(0xFF4CAF50) // Green
            icon = PlusCircle
            quantityPrefix = "+"
        }
        StockType.STOCK_OUT.toString() -> {
            itemColor = Color(0xFFF44336) // Red
            icon = MinusCircle
            quantityPrefix = "-"
        }
        else -> { // Default or unknown type
            itemColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // A neutral color
            icon = ImageVector.vectorResource(R.drawable.heroicons_inbox_arrow_down)
            quantityPrefix = ""
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mã: ${item.product.itemCode} | Đơn vị: ${item.product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!item.history.note.isNullOrEmpty()) {
                    Text(
                        text = "Note: ${item.history.note}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Date: ${formatDate(item.history.createdAt)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$quantityPrefix${item.history.quantity}",
                    style = MaterialTheme.typography.titleLarge,
                    color = itemColor,
                    fontWeight = FontWeight.Bold
                )
                // Số tồn kho sau nhập/xuất
                Text(
                    text = "Tồn kho: ${item.history.totalQuantityInInventory}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun HistoryItemStockInPreview() {
    MaterialTheme {
        HistoryItem(
            item = HistoryWithProduct(
                history = StockInHistory(
                    productId = "1",
                    quantity = 10,
                    totalQuantityInInventory = 100,
                    type = StockType.STOCK_IN.toString(),
                    note = "Received new shipment",
                    createdAt = System.currentTimeMillis()
                ),
                product = Product(
                    id = "1",
                    name = "Sample Product In",
                    stock = 100,
                    itemCode = TODO(),
                    unit = TODO(),
                    createdAt = TODO(),
                    updatedAt = TODO()
                )
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun HistoryItemStockOutPreview() {
    MaterialTheme {
        HistoryItem(
            item = HistoryWithProduct(
                history = StockInHistory(
                    productId = "2",
                    quantity = 5,
                    totalQuantityInInventory = 25,
                    type = StockType.STOCK_OUT.toString(),
                    note = "Sold to customer",
                    createdAt = System.currentTimeMillis()
                ),
                product = Product(
                    id = "2",
                    name = "Sample Product Out",
                    stock = 22,
                    itemCode = TODO(),
                    unit = TODO(),
                    createdAt = TODO(),
                    updatedAt = TODO()
                )
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HistoryListPreview() {
    MaterialTheme {
        val dummyHistory = listOf(
            HistoryWithProduct(
                history = StockInHistory(
                    productId = "1",
                    quantity = 10,
                    totalQuantityInInventory = 50,
                    type = StockType.STOCK_IN.toString(),
                    note = "Initial stock",
                    createdAt = System.currentTimeMillis()
                ),
                product = Product(
                    id = "1", name = "Product A", stock = 50,
                    itemCode = TODO(),
                    unit = TODO(),
                    createdAt = TODO(),
                    updatedAt = TODO()
                )
            ),
            HistoryWithProduct(
                history = StockInHistory(
                    productId = "2",
                    quantity = 5,
                    totalQuantityInInventory = 20,
                    type = StockType.STOCK_OUT.toString(),
                    note = "Customer order",
                    createdAt = System.currentTimeMillis()
                ),
                product = Product(
                    id = "2", name = "Product B", stock = 20,
                    itemCode = TODO(),
                    unit = TODO(),
                    createdAt = TODO(),
                    updatedAt = TODO()
                )
            ),
            HistoryWithProduct(
                history = StockInHistory(productId = "1", quantity = 7, totalQuantityInInventory = 57, type = StockType.STOCK_IN.toString(), createdAt = System.currentTimeMillis()),
                product = Product(
                    id = "1", name = "Product A", stock = 57,
                    itemCode = TODO(),
                    unit = TODO(),
                    createdAt = TODO(),
                    updatedAt = TODO()
                )
            )
        )
        HistoryList(historyItems = dummyHistory)
    }
}