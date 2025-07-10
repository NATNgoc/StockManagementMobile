package com.example.keygencetestapp.pages.stockOutPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.database.entities.ProcessOrderType
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.PurchaseOrderItem
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductIdWithCount
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductWithStockOutCount(
    val product: Product,
    val stockOutCount: Int
)

@HiltViewModel
class CreatePOViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao,
    private val purchaseOrderDao: PurchaseOrderDao
) : ViewModel() {
    private var _selectedProduct = MutableStateFlow<List<ProductWithStockOutCount>>(emptyList())
    val selectedProduct = _selectedProduct
    var _createPOState = MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle())
    val createPOState = _createPOState

    fun addProductToPO(item: ProductWithStockOutCount) {
        val currentList = _selectedProduct.value.toMutableList()
        val existingProductIndex = currentList.indexOfFirst { it.product.id == item.product.id }

        if (existingProductIndex != -1) {
            currentList[existingProductIndex] = currentList[existingProductIndex].copy(
                stockOutCount = currentList[existingProductIndex].stockOutCount + item.stockOutCount,
                product = currentList[existingProductIndex].product.copy(
                    stock = currentList[existingProductIndex].product.stock - item.stockOutCount
                )
            )
        } else {
            currentList.add(item.copy(product = item.product.copy(stock = item.product.stock - item.stockOutCount)))
        }
        _selectedProduct.value = currentList
    }

    fun createPO(note: String, startDate: Long, endDate: Long) {
        _createPOState.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
           try {
               val ids = selectedProduct.value.map { it.product.id }
               val productByIds = productDao.findProductWithIdsAndCount(ids).first()
               if (!isValidCountAndProductIds(productByIds,ids)) {
                   _createPOState.value = ResponseState.Error(false, "Product count is not enough!")
                   return@launch
               }
               val newPO = ProcessOrder(
                   note = note,
                   status = ProcessOrderStatus.NEW.toString(),
                   type = ProcessOrderType.STOCK_OUT.toString(),
                   planStartDate = startDate,
                   planEndDate = endDate
               )
               val newPOItems = createPurchaseOrderItemByPO(newPO.id, selectedProduct.value)
               purchaseOrderDao.insertPurchaseOrder(newPO)
               purchaseOrderItemDao.insertPurchaseOrderItems(newPOItems)
               updateProductStock(productByIds)
               delay(2000)
               _createPOState.value = ResponseState.Success(true, "Create PO Successfully!")
           } catch (ex: Exception) {
               _createPOState.value = ResponseState.Error(false, "Create PO Failed!")
           }
        }

    }

    private suspend fun updateProductStock(actualProducts: List<ProductIdWithCount>) {
        val mappedSelectedProduct = actualProducts.associate {
            it.id to it.count
        }
        val products = _selectedProduct.value.map {
            it.product.copy(stock = (mappedSelectedProduct[it.product.id] ?: 0) - it.stockOutCount)
        }
        productDao.updateProducts(products)
    }

    private suspend fun createPurchaseOrderItemByPO(poId: String, productIds: List<ProductWithStockOutCount>): List<PurchaseOrderItem> {
        val result: List<PurchaseOrderItem> = productIds.map {
            PurchaseOrderItem(
                processOrderId = poId,
                productId = it.product.id,
                quantity = it.stockOutCount
            )
        }
        return result
    }

    private suspend fun isValidCountAndProductIds(productByIds: List<ProductIdWithCount>,ids: List<String>): Boolean {

        if (productByIds.isEmpty()) return false
        if (productByIds.size != ids.size) return false
        val mappedSelectedProduct = _selectedProduct.value.associate {
            it.product.id to it.stockOutCount
        }
        for (productWithCount in productByIds) {
            val id = productWithCount.id
            val curCount = productWithCount.count
            val requiredCount = mappedSelectedProduct[id] ?: 0

            if (curCount < requiredCount) {
                return false
            }
        }
        return true
    }
}
