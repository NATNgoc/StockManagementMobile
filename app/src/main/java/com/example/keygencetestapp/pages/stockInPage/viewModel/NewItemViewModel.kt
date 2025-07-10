package com.example.keygencetestapp.pages.stockInPage.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val productDao: ProductDao
) : ViewModel() {

    private var _createProductState =
        MutableStateFlow<ResponseState<Boolean?>>(ResponseState.Idle())
    val createProductState: StateFlow<ResponseState<Boolean?>> = _createProductState
    private  var _initialProduct = MutableStateFlow<ResponseState<Product?>>(ResponseState.Idle())
    val initialProduct: StateFlow<ResponseState<Product?>> = _initialProduct

    fun getProductById(id: String): StateFlow<ResponseState<Product?>> {
        return productDao.findProductById(id)
            .map { it ->
                ResponseState.Success(it, "Success")
            }
            .catch { ex -> ResponseState.Error(null, "Lỗi khi tải sản phẩm: ${ex.message}") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResponseState.Loading<Product?>("Loading")
            )
    }

    fun getProductByItemCode(itemCode: String): StateFlow<ResponseState<Product?>> {
        return productDao.findProductByItemCode(itemCode)
            .map { it ->
                ResponseState.Success(it, "Success")
            }
            .catch { ex -> ResponseState.Error(null, "Lỗi khi tải sản phẩm: ${ex.message}") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResponseState.Loading<Product?>("Loading")
            )
    }

    fun getProductByItemCode2(itemCode: String): Unit {
        _initialProduct.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
            try {
                val product = productDao.findProductByItemCode(itemCode).firstOrNull()
                if (product != null) {
                    _initialProduct.value = ResponseState.Success(product, "Success")
                } else {
                    _initialProduct.value = ResponseState.Success(null, "Sản phẩm không tồn tại")
                }
            } catch (ex: Exception) {
                _initialProduct.value = ResponseState.Error(null, ex.message.toString())
            }
        }
    }


    fun createProduct(product: Product): Unit {
        _createProductState.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
            try {
                val existing = productDao.findProductByItemCode(product.itemCode).firstOrNull()
                Log.v("CreateP", existing.toString())
                if (existing != null) {
                    _createProductState.value = ResponseState.Error(
                        false,
                        "Sản phẩm đã tồn tại với mã: ${product.itemCode}"
                    )
                } else {
                    Log.v("CreateP", existing.toString())
                    productDao.createProduct(product)
                    _createProductState.value =
                        ResponseState.Success(true, "Thêm sản phẩm thành công")
                    _initialProduct.value = ResponseState.Success(product, "Success")
                }
            } catch (ex: Exception) {
                _createProductState.value =
                    ResponseState.Error(false, "Lỗi khi thêm sản phẩm: ${ex.message}")
            }
        }
    }
}
