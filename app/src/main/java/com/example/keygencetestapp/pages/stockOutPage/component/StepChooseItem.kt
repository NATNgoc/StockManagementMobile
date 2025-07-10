package com.example.keygencetestapp.pages.stockOutPage.component

import android.content.ClipData.Item
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme.colors
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.pages.stockOutPage.viewModel.SearchAndChooseItemViewModel
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.TablerIcons
import compose.icons.tablericons.AlertTriangle
import compose.icons.tablericons.ArrowDown
import compose.icons.tablericons.Box
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.Search
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonNull.content
import java.util.UUID

@Composable
fun StepChooseItem(modifier: Modifier = Modifier) {
    var debouncedSearchQuery by remember { mutableStateOf("") }
    Column(modifier) {
        NumberStep(1, "Choose Item Below")
        SearchProduct(debouncedSearchQuery = debouncedSearchQuery) {
            debouncedSearchQuery = it
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchProduct(
    createPOViewModel: SearchAndChooseItemViewModel = hiltViewModel<SearchAndChooseItemViewModel>(),
    modifier: Modifier = Modifier,
    debouncedSearchQuery: String,
    onDebouncedSearchQueryChange: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val products = createPOViewModel.allProductState.collectAsState()


    LaunchedEffect(Unit) {
        createPOViewModel.getAllProducts()
    }

    LaunchedEffect(searchQuery) {
        delay(2000) // 2 seconds debounce
        onDebouncedSearchQueryChange(searchQuery.text)
        if (debouncedSearchQuery.isNotEmpty()) {
            println("Searching for: $debouncedSearchQuery")
        }
    }

    Surface(modifier) {
        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search products") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(imageVector = TablerIcons.Search, null)
                },
                maxLines = 1,
                shape = RoundedCornerShape(20.dp)
            )
            if (debouncedSearchQuery.isNotEmpty()) {
                Text(
                    "Search results for: $debouncedSearchQuery",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (products.value) {
                    is ResponseState.Loading -> {
                        LoadingIndicator(Modifier.align(Alignment.Center))
                    }

                    is ResponseState.Success -> {
                        if ((products.value as ResponseState.Success<List<Product>?>).data != null) {
                            ProductList((products.value as ResponseState.Success<List<Product>>).data)
                        }
                    }

                    is ResponseState.Error -> {
                        Text("Error: ${(products.value as ResponseState.Error<List<Product>?>).message}")
                    }

                    else -> {}

                }
            }

        }
    }
}

data class ItemList(
    val id: String = UUID.randomUUID().toString(),
    val product: Product,
    val isSelected: Boolean
)

@Composable
fun ProductList(
    products: List<Product>,
    modifier: Modifier = Modifier,
    createPOViewModel: SearchAndChooseItemViewModel = hiltViewModel()
) {

    var items by remember {
        mutableStateOf(products.map {
            ItemList(product = it, isSelected = false)
        })
    }

    val onClickItem = { itemSelected: ItemList ->
        createPOViewModel.setSelectedProduct(itemSelected.product)
        items = items.map {
            if (it.id == itemSelected.id) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
    }
    LazyColumn(modifier) {
        items(items) { item ->
            ItemCard(
                product = item.product,
                isSelected = item.isSelected,
                onClick = { onClickItem(item) },
                status = when {
                    item.product.stock == 0 -> ItemStatus.OUT_OF_STOCK
                    item.product.stock  < 10 -> ItemStatus.LOW_STOCK
                    else -> ItemStatus.IN_STOCK
                }
            )
        }
    }

}

enum class ItemStatus {
    OUT_OF_STOCK,
    LOW_STOCK,
    IN_STOCK
}

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    status: ItemStatus = ItemStatus.OUT_OF_STOCK
) {

    val backgroundColor = if (isSelected) Color.DarkGray else MaterialTheme.colorScheme.background
    val borderColor = if (isSelected) Color.Black else Color.LightGray
    val padding by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 8.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val OutStockBadge = @Composable { Badge(
        modifier = modifier
            .padding(top = 8.dp)
            .background(Color(0xFFFFE5EC), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        containerColor = Color.Transparent,
        contentColor = Color(0xFFFF0033)
    ) {
        Icon(TablerIcons.AlertTriangle, null)
        Text("Out of stock")
    }}

    val LowStockBadge = @Composable { Badge(
        modifier = modifier
            .padding(top = 8.dp)
            .background(Color(0xFFFFF4E5), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        containerColor = Color.Transparent,
        contentColor = Color(0xFFFF8800)
    ) {
        Icon(TablerIcons.ArrowDown, null)
        Text("Low of stock")
    }}

    val InStock = @Composable { Badge(
        modifier = modifier
            .padding(top = 8.dp)
            .background(Color(0xFFE8FFF3), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        containerColor = Color.Transparent,
        contentColor = Color(0xFF17B963)
    ) {
        Icon(TablerIcons.CircleCheck, null)
        Text("In stock")
    }}

    Surface(
        modifier = modifier
            .padding(padding)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(enabled = status != ItemStatus.OUT_OF_STOCK) {
                onClick()
            }
            .alpha(if (status == ItemStatus.OUT_OF_STOCK) 0.5f else 1f),
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .width(280.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        product.itemCode,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        product.name,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
                Row {
                    if (isSelected) {
                        Icon(
                            imageVector = TablerIcons.CircleCheck,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = TablerIcons.Box,
                        contentDescription = null,
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        product.stock.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                    Text(
                        product.unit.uppercase(),
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                if (isLowStock) {
//                    LowStockBadge()
//                } else {
//                    InStockBadge()
//                }
                when(status) {
                    ItemStatus.OUT_OF_STOCK -> OutStockBadge()
                    ItemStatus.LOW_STOCK -> LowStockBadge()
                    ItemStatus.IN_STOCK -> InStock()
                }
            }


        }
    }
}

@Composable
fun SmallItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    isSelected: Boolean = false,
    stockOutCount: Int = 0,
    onClick: () -> Unit
) {

    val backgroundColor = if (isSelected) Color.DarkGray else MaterialTheme.colorScheme.background
    val borderColor = if (isSelected) Color.Black else Color.LightGray
    val padding by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 8.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Surface(
        modifier = modifier
            .padding(padding)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable {
                onClick()
            },
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .width(280.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        product.itemCode,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        product.name,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Row {
                    if (isSelected) {
                        Icon(
                            imageVector = TablerIcons.CircleCheck,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = TablerIcons.Box,
                        contentDescription = null,
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        product.stock.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp
                    )
                    Text(
                        product.unit.uppercase(),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            if (stockOutCount > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFFFEBEE),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = TablerIcons.ArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "XUẤT KHO",
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                text = "-$stockOutCount",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp)
                            )

                            Text(
                                text = product.unit.uppercase(),
                                color = Color(0xFFD32F2F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Còn lại: ${product.stock - stockOutCount} ${product.unit.uppercase()}",
                                color = Color(0xFFDC4949),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                        }
                    }
            }
        }
    }
}

@Composable
fun InStockBadge(modifier: Modifier = Modifier) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Badge(containerColor = Color(0xFF81CC81)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(TablerIcons.CircleCheck, null, tint = Color(0xFF2A542B))
                Text(
                    "In Stock",
                    color = Color(0xFF2A542B),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LowStockBadge(modifier: Modifier = Modifier) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Badge(containerColor = Color(0xFFCE6157)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(TablerIcons.AlertTriangle, null, tint = Color(0xFF8C2D27))
                Text(
                    "Low Stock",
                    color = Color(0xFF8C2D27),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




