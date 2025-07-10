package com.example.keygencetestapp.pages.HistoryPage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryDao
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryWithProduct
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private  val historyDao: HistoryDao,
) : ViewModel() {
    private val _stockHistory: MutableStateFlow<ResponseState<List<HistoryWithProduct>>> =
        MutableStateFlow(ResponseState.Idle(emptyList()))
    val stockHistory = _stockHistory

    fun getHistories() {
        _stockHistory.value = ResponseState.Loading("Loading")
        viewModelScope.launch {
            try {
                val stockHistories = historyDao.getAllHistoriesWithProduct().firstOrNull()
                if (stockHistories != null) {
                    _stockHistory.value = ResponseState.Success(stockHistories, "Success")
                } else {
                    _stockHistory.value = ResponseState.Success(emptyList(), "No history found")
                }
            } catch (exp: Exception) {
                _stockHistory.value = ResponseState.Error(emptyList(), exp.message.toString())
            }
        }

    }

}



