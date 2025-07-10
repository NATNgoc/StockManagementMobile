package com.example.keygencetestapp.pages.stockOutPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAndChooseItemViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao,
    private val purchaseOrderDao: PurchaseOrderDao
) : ViewModel() {

    private var _allProductState =
        MutableStateFlow<ResponseState<List<Product>?>>(ResponseState.Idle())
    val allProductState = _allProductState
    private var _selectedProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    val selectedProduct: MutableStateFlow<Product?> = _selectedProduct
    private var numberStockOut = 0

    fun setSelectedProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun setNumberStockOut(number: Int) {
        numberStockOut = number
    }

    fun getNumberStockOut(): Int {
        return numberStockOut
    }

    fun resetSelectedProduct() {
        _selectedProduct.value = null
        numberStockOut = 0
    }

    fun decreaseStockQuantity() {
        val currentProductList = _allProductState.value
        if (_selectedProduct.value != null && currentProductList is ResponseState.Success) {
            val updatedList = currentProductList.data!!.map { product ->
                if (product.id == _selectedProduct.value!!.id) {
                    product.copy(stock = product.stock - numberStockOut)
                } else {
                    product
                }
            }
            _allProductState.value = ResponseState.Success(updatedList, "Success")

        }
    }

    fun getAllProducts() {
        if (_allProductState.value is ResponseState.Success) return

        _allProductState.value = ResponseState.Loading("Loading")

        viewModelScope.launch {
            try {
                delay(1000)
                val products = productDao.findAllProduct().firstOrNull()
                if (products != null) {
                    _allProductState.value = ResponseState.Success(products.sortedByDescending {
                        it.stock
                    }, "Success")
                } else {
                    _allProductState.value = ResponseState.Success(null, "Nothing here!")
                }
            } catch (ex: Exception) {
                _allProductState.value = ResponseState.Error(null, ex.message.toString())
            }
        }
    }
}
