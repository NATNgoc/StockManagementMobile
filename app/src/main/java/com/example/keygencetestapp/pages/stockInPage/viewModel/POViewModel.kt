package com.example.keygencetestapp.pages.stockInPage.viewModel

import android.util.Log
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.constant.DefaultPurchaseOrder
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.database.entities.ProcessOrderType
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class POViewModel @Inject constructor(
    private val purchaseOrderDao: PurchaseOrderDao,
    private val productDao: ProductDao
) : ViewModel() {

    private var _defaultPO = MutableStateFlow<ResponseState<ProcessOrder?>>(ResponseState.Idle())
    val defaultPO = _defaultPO


    fun getDefaultPurchaseOrder(): StateFlow<ResponseState<ProcessOrder?>> {
        return purchaseOrderDao.getOrderById(DefaultPurchaseOrder.ID)
            .map { po -> ResponseState.Success(po, "Success") }
            .catch { exception ->
                ResponseState.Error(null, "Lỗi khi tải đơn hàng mặc định: ${exception.message}")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResponseState.Loading("Loading")
            )
    }

    fun getDefaultPurchaseOrder2(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
                purchaseOrderDao.getOrderById(DefaultPurchaseOrder.ID)
                    .map { po ->
                        if (po != null) ResponseState.Success(po, "Tìm thấy sản phẩm")
                        else ResponseState.Error(null, "Không tìm thấy sản phẩm")
                    }
                    .catch { exception ->
                        _defaultPO.value = ResponseState.Error(null, "Lỗi khi tải đơn hàng mặc định: ${exception.message}")
                    }
                    .collect {
                        _defaultPO.value = it as ResponseState<ProcessOrder?>
                    }
        }
    }

    fun insertStockInPO() {
        viewModelScope.launch(Dispatchers.IO) {
            val defaultPO = ProcessOrder(
                DefaultPurchaseOrder.ID,
                DefaultPurchaseOrder.NOTE,
                DefaultPurchaseOrder.STATUS,
                type = ProcessOrderType.STOCK_IN.toString(),
                planStartDate = DefaultPurchaseOrder.CREATED_AT.toLong(),
                planEndDate = DefaultPurchaseOrder.UPDATED_AT.toLong(),
                createdAt = DefaultPurchaseOrder.CREATED_AT.toLong(),
                updatedAt = DefaultPurchaseOrder.UPDATED_AT.toLong()
            )
            try {
                purchaseOrderDao.insertPurchaseOrder(defaultPO)
            } catch (e: Exception) {
                // Xử lý lỗi
                Log.e("POViewModel", "Error inserting order", e)
            }
        }
    }

}