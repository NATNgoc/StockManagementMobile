package com.example.keygencetestapp.pages.stockOutPage

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keygencetestapp.components.PlaceHolderScreen
import com.example.keygencetestapp.components.PortableLoading
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.pages.stockInPage.DAO.POWithTotalItems
import com.example.keygencetestapp.pages.stockOutPage.component.DashBoardTab
import com.example.keygencetestapp.pages.stockOutPage.component.ManageTab
import com.example.keygencetestapp.pages.stockOutPage.component.StockOUtNavigationBar
import com.example.keygencetestapp.pages.stockOutPage.viewModel.DashboardViewModel
import com.example.keygencetestapp.utils.ResponseState

object StockOutTabs {
    const val DASHBOARD = 0
    const val MANAGE = 1
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun StockOutPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    defaultScreen: Int = StockOutTabs.DASHBOARD,
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {
    var selectedItem by remember { mutableIntStateOf(defaultScreen) }
    val processOrdersState = dashboardViewModel.processOrderWithTotalItems.collectAsState()
    BackHandler {
        navController.navigate(Routes.HOME_SCREEN)
    }
    LaunchedEffect(Unit) {
        dashboardViewModel.getProcessOrderWithTotalItems()
    }
    Scaffold(
        bottomBar = {
            StockOUtNavigationBar(selectedItem) {
                selectedItem = it
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Surface(modifier.fillMaxSize()) {

                when (val processOrders = processOrdersState.value) {
                    is ResponseState.Success<List<POWithTotalItems>> -> {
                        when (selectedItem) {
                            0 -> DashBoardTab(
                                processOrders = (processOrders as ResponseState.Success<List<POWithTotalItems>>).data.map {
                                    it.processOrder
                                }
                            )

                            1 -> ManageTab(
                                navController = navController,
                                poList = (processOrders as ResponseState.Success<List<POWithTotalItems>>).data,
                            )
                        }
                    }

                    is ResponseState.Loading<*> -> {
                        PortableLoading()
                    }

                    else -> {
                        PlaceHolderScreen()
                    }
                }
            }

        }
    }
}







