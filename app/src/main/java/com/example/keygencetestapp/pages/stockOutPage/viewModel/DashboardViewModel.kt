package com.example.keygencetestapp.pages.stockOutPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.pages.stockInPage.DAO.POWithPOItems
import com.example.keygencetestapp.pages.stockInPage.DAO.POWithTotalItems
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao,
    private val purchaseOrderDao: PurchaseOrderDao
) : ViewModel() {

    private var _processOrder = MutableStateFlow<ResponseState<List<ProcessOrder>>>(ResponseState.Idle())
    val processOrder = _processOrder

    private var _processOrderWithPoItems = MutableStateFlow<ResponseState<List<POWithTotalItems>>>(ResponseState.Idle())
    val processOrderWithTotalItems = _processOrderWithPoItems

    fun getProcessOrder() {
        _processOrder.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
            try {
                val pOs = purchaseOrderDao.getAllOrders().first()
                if (pOs.isEmpty()) _processOrder.value = ResponseState.Success(pOs, "Nothing here!")
                _processOrder.value = ResponseState.Success(pOs, "Success")
            } catch(ex: Exception) {
                _processOrder.value = ResponseState.Error(null, ex.message.toString())
            }
        }
    }

    fun getProcessOrderWithTotalItems() {
        _processOrderWithPoItems.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
            try {
                val result = purchaseOrderDao.getPOwithTotalItems()
                if (result.isEmpty()) _processOrderWithPoItems.value = ResponseState.Success(result, "Nothing here!")
                _processOrderWithPoItems.value = ResponseState.Success(result, "Success")
            } catch (ex:Exception) {
                _processOrderWithPoItems.value = ResponseState.Error(null, ex.message.toString())
            }
        }
    }
}