import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keygencetestapp.PODetailScreen
import com.example.keygencetestapp.StockOutAction
import com.example.keygencetestapp.components.PlaceHolderScreen
import com.example.keygencetestapp.components.ShadowCard
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.pages.stockInPage.DAO.FullProcessOrder
import com.example.keygencetestapp.pages.stockOutPage.StockOutTabs
import com.example.keygencetestapp.pages.stockOutPage.component.PoItem
import com.example.keygencetestapp.pages.stockOutPage.component.stockOutAction.ProductScanList
import com.example.keygencetestapp.pages.stockOutPage.type.ItemStockOutDetailProgress

import com.example.keygencetestapp.pages.stockOutPage.viewModel.PODetailViewModel
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.TablerIcons
import compose.icons.tablericons.Box

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PoDetailsScreen(
    navController: NavController,
    id: String,
    modifier: Modifier = Modifier,
    viewModel: PODetailViewModel = hiltViewModel()
) {
    val poDetailState by viewModel.poDetailState.collectAsState()
    val itemsStockOutProgress by viewModel.stockOutProgress.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getPOById(id)
    }
    BackHandler {
        navController.navigate(
            com.example.keygencetestapp.StockOutScreen(tab = StockOutTabs.MANAGE)
        ) {
        }
    }
    Surface(modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            when (val state = poDetailState) {
                is ResponseState.Loading ->
                    ContainedLoadingIndicator()

                is ResponseState.Success ->
                    state.data?.let { po ->
                        POInfo(navController, po, itemsStockOutProgress.listItemProgress)
                    }

                is ResponseState.Error ->
                    PlaceHolderScreen()

                else ->
                    PlaceHolderScreen()
            }
        }
    }
}

@Composable
fun POInfo(
    navController: NavController,
    po: FullProcessOrder,
    items: List<ItemStockOutDetailProgress>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        PoItem(po = po.order, modifier = Modifier, quantity = items.size)
        ProductScanList(items, Modifier
            .padding(10.dp)
            .weight(1f))
        Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (po.order.status != ProcessOrderStatus.COMPLETED.toString()) {
                Button(onClick = {
                    navController.navigate(
                        StockOutAction(
                            id = po.order.id
                        )
                    )
                }, modifier = Modifier) {
                    Text("Stock out")
                }
            }
        }
    }
}



