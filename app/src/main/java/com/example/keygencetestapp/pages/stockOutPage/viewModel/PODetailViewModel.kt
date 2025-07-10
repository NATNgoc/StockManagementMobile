package com.example.keygencetestapp.pages.stockOutPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.database.entities.StockInHistory
import com.example.keygencetestapp.database.entities.StockType
import com.example.keygencetestapp.pages.stockInPage.DAO.FullProcessOrder
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryDao
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import com.example.keygencetestapp.pages.stockOutPage.type.ItemStockOutDetailProgress
import com.example.keygencetestapp.pages.stockOutPage.type.StockOutActionProgress
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PODetailViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao,
    private val purchaseOrderDao: PurchaseOrderDao,
    private val historyDao: HistoryDao
) : ViewModel() {

    private var _poDetailState =
        MutableStateFlow<ResponseState<FullProcessOrder?>>(ResponseState.Idle())
    val poDetailState = _poDetailState
    private var _stockOutProgrss = MutableStateFlow<StockOutActionProgress>(
        StockOutActionProgress(
            0, 0,
            listItemProgress = arrayListOf()
        )
    )
    val stockOutProgress = _stockOutProgrss
    private var _resultScan = MutableStateFlow<ResponseState<String>>(ResponseState.Idle())
    val resultScan = _resultScan
    private var _isFinisedScan = MutableStateFlow<Boolean>(false)
    val isFinisedScan = _isFinisedScan
    private var _stockOutState = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle())
    val stockOutState = _stockOutState
    //-----------------------------------------------------------------------------------------------
    fun getPOById(id: String) {
        _poDetailState.value = ResponseState.Loading("Loading")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result: FullProcessOrder? = purchaseOrderDao.getPOwithPOItemsAndProductById(id)
                if (result == null) {
                    _poDetailState.value = ResponseState.Error(null, "Nothing here!")
                    return@launch
                }
                _poDetailState.value = ResponseState.Success(result, "Success")

                mappingFullProcessOrderToStockOutProgress(result, isCompletedCase = result.order.status == ProcessOrderStatus.COMPLETED.toString())
            } catch (ex: Exception) {
                _poDetailState.value = ResponseState.Error(null, ex.message.toString())

            }
        }
    }

    fun stockOutProduct() {
        _stockOutState.value = ResponseState.Loading("Loading")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val poDetail =
                    (_poDetailState.value as? ResponseState.Success<FullProcessOrder>)?.data
                if (poDetail != null) {
                    insertStockOutHistory(poDetail)
                    purchaseOrderDao.updatePOStatus(poDetail.order.id, ProcessOrderStatus.COMPLETED)
                    _stockOutState.value = ResponseState.Success(true, "Success Stock out")
                    return@launch
                }
                _stockOutState.value = ResponseState.Error(false, "Error")
            } catch (ex: Exception) {
                _stockOutState.value = ResponseState.Error(false, ex.message.toString())
            }
        }
    }

    private suspend fun insertStockOutHistory(poDetail: FullProcessOrder) {
        val historiesToInsert = mutableListOf<StockInHistory>()
        poDetail.items.forEach { itemProgress ->
            historiesToInsert.add(
                StockInHistory(
                    productId = itemProgress.product.id,
                    quantity = itemProgress.item.quantity,
                    type = StockType.STOCK_OUT.toString(),
                    totalQuantityInInventory = itemProgress.product.stock
                )
            )
        }
        if (historiesToInsert.isNotEmpty()) {
            historyDao.insertHistories(historiesToInsert.toList())
        }

    }

    private fun mappingFullProcessOrderToStockOutProgress(fullProcessOrder: FullProcessOrder, isCompletedCase: Boolean = false) {
        var totalRemain = 0
        var items = listOf<ItemStockOutDetailProgress>()
        if (isCompletedCase) {
            items = fullProcessOrder.items.map {
                totalRemain += it.item.quantity
                ItemStockOutDetailProgress(
                    id = it.product.id,
                    name = it.product.name,
                    itemCode = it.product.itemCode!!,
                    required = it.item.quantity,
                    scanned = it.item.quantity,
                    remain = 0
                )
            }
        } else {
            items = fullProcessOrder.items.map {
                totalRemain += it.item.quantity
                ItemStockOutDetailProgress(
                    id = it.product.id,
                    name = it.product.name,
                    itemCode = it.product.itemCode!!,
                    required = it.item.quantity,
                    scanned = 0,
                    remain = it.item.quantity
                )
            }
        }
        _stockOutProgrss.value = StockOutActionProgress(
            remain = if (!isCompletedCase) totalRemain else 0,
            required = totalRemain,
            totalScanned = 0,
            listItemProgress = ArrayList(items)
        )
    }

    fun resetFinished() {
        _isFinisedScan.value = false
    }

    fun scanItemId(id: String) {
        val currentState = _stockOutProgrss.value
        val indexItem = currentState.listItemProgress.indexOfFirst {
            it.id == id
        }
        if (indexItem == -1) {
            _resultScan.value = ResponseState.Error<String>(null, "Item not exist")
            return
        }
        val curItem = currentState.listItemProgress[indexItem]
        if (curItem.remain == 0) {
            _resultScan.value =
                ResponseState.Error<String>(null, "Already finished! Don't need to scan!")
            return
        }
        val updateItem = curItem.copy(
            scanned = curItem.scanned + 1, remain = curItem.remain - 1
        )
        val newList = currentState.listItemProgress.toMutableList().apply {
            set(indexItem, updateItem)
        }
        if (currentState.remain - 1 == 0) _isFinisedScan.value = true
        _stockOutProgrss.value = currentState.copy(
            remain = currentState.remain - 1,
            totalScanned = currentState.totalScanned + 1,
            listItemProgress = newList
        )
        _resultScan.value = ResponseState.Success<String>("", "Successfully updated")
    }

    fun resetScanState() {
        resultScan.value = ResponseState.Idle()
    }


}