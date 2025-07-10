package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.example.keygencetestapp.R
import com.example.keygencetestapp.components.PlaceHolderScreen
import com.example.keygencetestapp.components.PortableLoading
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.pages.stockOutPage.viewModel.DashboardViewModel
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.TablerIcons
import compose.icons.tablericons.Box
import compose.icons.tablericons.CalendarTime
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.Pencil
import java.math.RoundingMode

data class ItemDashBoardColor(
    val backgroundColor: Color,
    val titleColor: Color,
)

@Composable
fun DashBoardTab(modifier: Modifier = Modifier,processOrders: List<ProcessOrder>) {
    val scrollState = rememberScrollState()
    Surface(modifier.fillMaxSize()) {
        DashBoardWithPOData(processOrders,scrollState)
    }
}

@Composable
private fun DashBoardWithPOData(pOs: List<ProcessOrder>,scrollState: ScrollState) {
    val totalPO = pOs.size
    val pendingPO = pOs.filter { it.status == ProcessOrderStatus.NEW.name }.size
    val completedPO = totalPO - pendingPO
    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .verticalScroll(state = scrollState), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ItemDashBoard(
            Modifier, "Total PO",totalPO.toString(), colors = ItemDashBoardColor(
                backgroundColor = Color(0xFF2196F3),
                titleColor = Color(0xFFE6E8E8)
            ),
            icon = {
                Icon(
                    TablerIcons.Box,
                    null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFFE6E8E8)
                )
            }
        ) {
            Row(
                Modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.stock_up),
                    null,
                    tint = Color(0xFFE6E8E8)
                )
                Text("Increase better than last month")
            }
        }
        ItemDashBoard(
            Modifier, "Pending PO", pendingPO.toString(), colors = ItemDashBoardColor(
                backgroundColor = Color(0xFFF38D21),
                titleColor = Color(0xFFE6E8E8)
            ),
            icon = {
                Icon(
                    TablerIcons.CalendarTime,
                    null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFFE6E8E8)
                )
            }
        ) {
            Row(
                Modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(ImageVector.vectorResource(R.drawable.timer), null, tint = Color(0xFFE6E8E8))
                Text("Waiting for resolving")
            }
        }
        ItemDashBoard(
            Modifier, "Completed", completedPO.toString(), colors = ItemDashBoardColor(
                backgroundColor = Color(0xFF1DA614),
                titleColor = Color(0xFFE6E8E8)
            ),
            icon = {
                Icon(
                    TablerIcons.Pencil,
                    null,
                    tint = Color(0xFFE6E8E8),
                    modifier = Modifier.size(40.dp)
                )
            }
        ) {
            Row(
                Modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(TablerIcons.CircleCheck, null, tint = Color(0xFFE6E8E8))
                val percentage = if (totalPO > 0) (completedPO.toDouble() / totalPO.toDouble()).toBigDecimal().setScale(2, RoundingMode.HALF_UP).multiply(100.toBigDecimal()) else 0.toBigDecimal()
                Text(" $percentage% percent")
            }
        }
    }
}




@Composable
fun ItemDashBoard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    colors: ItemDashBoardColor,
    icon: @Composable () -> Unit = {},
    extraBadge: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier.padding(vertical = 8.dp),
        color = Color.Transparent // Surface transparent để shadow rõ nét hơn
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colors.backgroundColor.copy(alpha = 0.7f),
                            colors.backgroundColor
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = CardColors(
                containerColor = colors.backgroundColor,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp  // ← Tăng giá trị này để shadow mờ và rõ nét hơn
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.titleColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    extraBadge()
                }
                icon()
            }
        }
    }
}



//@Composable
//fun ItemDashBoard(
//    modifier: Modifier = Modifier,
//    title: String,
//    value: String,
//    colors: ItemDashBoardColor,
//    icon: @Composable () -> Unit = {},
//    extraBadge: @Composable () -> Unit = {},
//) {
//    Surface(
//        modifier = modifier,
//        shape = RoundedCornerShape(10.dp),
//        shadowElevation = 10.dp,
//        // Dùng Brush horizontalGradient làm background
//        color = Color.Transparent // set trong Brush nên để trong suốt
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    brush = Brush.horizontalGradient(
//                        colors = listOf(
//                            colors.backgroundColor.copy(alpha = 0.3f), // Màu nhạt bên trái
//                            colors.backgroundColor // Màu đậm hơn bên phải
//                        )
//                    ),
//                    shape = RoundedCornerShape(10.dp)
//                )
//                .padding(20.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    Modifier.background(color = Color.Transparent)
//                ) {
//                    Text(
//                        title,
//                        style = MaterialTheme.typography.titleMedium,
//                        color = colors.titleColor,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        value,
//                        style = MaterialTheme.typography.displayMedium,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = Color.White
//                    )
//                    extraBadge()
//                }
//                icon()
//            }
//        }
//    }
//}
