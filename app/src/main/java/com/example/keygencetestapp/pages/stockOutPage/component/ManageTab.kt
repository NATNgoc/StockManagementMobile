package com.example.keygencetestapp.pages.stockOutPage.component

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.keygencetestapp.PODetailScreen
import com.example.keygencetestapp.R
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.database.entities.ProcessOrderType
import com.example.keygencetestapp.pages.stockInPage.DAO.POWithTotalItems
import com.example.keygencetestapp.utils.getLastUpdatedString
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowRight
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.Plus
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManageTab(
    modifier: Modifier = Modifier,
    navController: NavController,
    poList: List<POWithTotalItems>
) {
    val listState = rememberLazyListState()
    val expandedFab by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                SearchBarPO()
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.STOCK_OUT_CREATE_PO) },
                expanded = expandedFab,
                icon = { Icon(TablerIcons.Plus, "Localized Description") },
                text = { Text(text = "Create PO") },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Surface(Modifier.fillMaxSize()) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(items = poList.sortedByDescending {
                    it.processOrder.updatedAt
                }) {
                    PoItem(po = it.processOrder, quantity = it.countItems) {
                        navController.navigate(
                            PODetailScreen(
                                id = it.processOrder.id
                            )
                        )
                    }
                }
            }
        }

    }
}

val sdf = SimpleDateFormat("dd/MM/yyyy")

@Composable
fun PoItem(
    modifier: Modifier = Modifier,
    po: ProcessOrder,
    quantity: Int = 998,
    unit: String = "Items",
    location: String = "Warehouse No. 1",
    onClick: () -> Unit = {}
) {

    Box(modifier
        .padding(8.dp)
        .clickable {
            onClick()
        }) {
        Icon(
            ImageVector.vectorResource(R.drawable.delivery_truck),
            null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .alpha(0.10f)
                .zIndex(0.5f)
                .size(150.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false
                )
                .zIndex(0.4f),
            colors = CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .zIndex(0.7f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "#PO",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                    )
                    Text(
                        text = po.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = TablerIcons.Calendar,
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = sdf.format(Date(po.planStartDate)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = TablerIcons.ArrowRight,
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = sdf.format(Date(po.planEndDate)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.time_update),
                        contentDescription = "Last Update At",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = getLastUpdatedString(po.updatedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = unit,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    when (po.status) {
                        ProcessOrderStatus.NEW.name -> NewStatus(modifier.zIndex(1f))
                        else -> CompletedStatus(modifier.zIndex(1f))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.location),
                            contentDescription = "Kho",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = location,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PoItemPreview() {
    PoItem(
        po = ProcessOrder(
            id = "PO-20231026-001",
            status = ProcessOrderStatus.NEW.name,
            planStartDate = System.currentTimeMillis(),
            planEndDate = System.currentTimeMillis() + 86400000,
            updatedAt = System.currentTimeMillis() - 3600000,
            note = "asdasdds",
            type = ProcessOrderType.STOCK_OUT.toString(),
            createdAt = System.currentTimeMillis() - 3600000
        ),
        quantity = 150,
        unit = "Boxes",
        location = "Warehouse No. 2"
    )
}