package com.example.keygencetestapp.pages.stockInPage.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.api.SmapriApi
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryDao
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.StockInHistory
import com.example.keygencetestapp.database.entities.StockType
import com.example.keygencetestapp.pages.stockInPage.constant.DefaultPurchaseOrder
import com.example.keygencetestapp.utils.ResponseState
import com.example.keygencetestapp.utils.convertMillisToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StockInViewModel @Inject constructor(
    private  val historyDao: HistoryDao,
    private  val productDao: ProductDao,
    private  val smapriApi: SmapriApi
): ViewModel() {

    private var _stockInState = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle(false))
    val stockInState: StateFlow<ResponseState<Boolean>> = _stockInState

    fun stockInItem(item: Product, quantity: Int, note: String? = "") {
        _stockInState.value = ResponseState.Loading("Loading")
        viewModelScope.launch { 
            try {
                val isExistingProduct = productDao.findProductById(item.id).firstOrNull()
                if (isExistingProduct == null) {
                    _stockInState.value = ResponseState.Error(false, "Sản phẩm không tồn tại")
                    return@launch
                }
                val newQuantity = isExistingProduct.stock + quantity
                productDao.updateProductQuantityById(id = isExistingProduct.id, newQuantity)
                historyDao.insertHistory(
                    StockInHistory(
                        productId = isExistingProduct.id,
                        quantity = quantity,
                        note = note,
                        type = StockType.STOCK_IN.toString(),
                        totalQuantityInInventory = newQuantity
                    )
                )
                _stockInState.value = ResponseState.Success(true, "Success")
                val response = smapriApi.sendStockInPrintRequest(
                    archiveUrl = "file:///storage/emulated/0/Download/SATO/test.spfmtz",
                    formatIdNumber = 1,
                    productId = item.id,
                    quantity = item.unit,
                    poId = DefaultPurchaseOrder.ID,
                    productName = item.name,
                    receivedDate = convertMillisToDate(item.createdAt),
                    productCode = item.itemCode,
                    quantityCopy = quantity
                )
                if (response.isSuccessful) {
                    Log.v("Result API", "Print request sent successfully ${response.body()}")
                } else {
                    Log.e("Result API", "Print request failed: ${response.errorBody()?.string()}")
                }


            } catch (exp: Exception) {
                Log.e("Result API", "Print request failed: ${exp.message.toString()}]")
                _stockInState.value = ResponseState.Error(false, exp.message.toString())
            }
        }
    }

}