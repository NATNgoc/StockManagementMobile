package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.pages.stockOutPage.viewModel.SearchAndChooseItemViewModel

@Composable
fun StepConfirmStockOutItem(modifier: Modifier = Modifier, createPOViewModel: SearchAndChooseItemViewModel = hiltViewModel()) {
    val product = createPOViewModel.selectedProduct.collectAsState()
    Surface {
        Column {
            SmallItemCard(product = product.value!!, isSelected = false, onClick = {}, stockOutCount = createPOViewModel.getNumberStockOut())
        }
    }
}