package com.example.keygencetestapp.pages.SettingPage.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keygencetestapp.DI.retrofit.BaseUrlHolder
import com.example.keygencetestapp.api.SmapriApi
import com.example.keygencetestapp.constant.ParameterConstant
import com.example.keygencetestapp.constant.RetrofitConstant
import com.example.keygencetestapp.database.DAO.ParameterDao
import com.example.keygencetestapp.database.entities.Parameters
import com.example.keygencetestapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val parameterDao: ParameterDao,
    private val smapriApi: SmapriApi,
    private val baseUrlHolder: BaseUrlHolder
) : ViewModel() {

    private var _checkConnectionState= MutableStateFlow<ResponseState<Boolean>>(ResponseState.Idle())
    val checkConnectionState = _checkConnectionState
    private var _initialIpAddressState = MutableStateFlow<ResponseState<String>>(ResponseState.Idle())
    val initialIpAddressState = _initialIpAddressState
    fun checkConnection(ipAddress: String) {
        _checkConnectionState.value = ResponseState.Loading("Loading")

        viewModelScope.launch {

            try {
                val curIpAddress = parameterDao.getValueByKey(ParameterConstant.KEY_PRINTER_IP_ADDRESS)?.value
                checkAndUpdateNewIpAddress(curIpAddress, ipAddress)

                val response = smapriApi.getPrinterStatus()
                if (isSuccessfulStatus(response.body()?.string())) {
                    _checkConnectionState.value = ResponseState.Success(true, "Success status")
                    smapriApi.sendSamplePrintRequest()
                    return@launch
                }
                _checkConnectionState.value = ResponseState.Error(true, "Unknown Error")
            } catch(ex: Exception) {
                _checkConnectionState.value = ResponseState.Error(null, "Unknown Error")
            }
        }
    }

    fun getIpAddress() {
        initialIpAddressState.value = ResponseState.Loading<String>("Loading")
        viewModelScope.launch {
            val curIpAddress = parameterDao.getValueByKey(ParameterConstant.KEY_PRINTER_IP_ADDRESS)?.value ?: RetrofitConstant.SMAPRI_BASE_URL
            initialIpAddressState.value = ResponseState.Success<String>(curIpAddress,"Success")

        }
    }

    private suspend fun checkAndUpdateNewIpAddress(curIpAddress: String?, ipAddress: String) {
        if (curIpAddress == null) {
            val newParam =
                Parameters(key = ParameterConstant.KEY_PRINTER_IP_ADDRESS, value = ipAddress)
            parameterDao.insertParameter(newParam)
        } else if (curIpAddress != ipAddress) {
            parameterDao.updateValueByKey(
                ParameterConstant.KEY_PRINTER_IP_ADDRESS,
                value = ipAddress
            )
        }
        baseUrlHolder.baseUrl = ipAddress
    }

    fun refreshState() {
        _checkConnectionState.value = ResponseState.Idle()
    }

    private fun isSuccessfulStatus(body: String?): Boolean {
        Log.v("Log", body ?:"" )
        if (body != null) {
            return body.contains("<result>OK</result>", ignoreCase = true)
        }
        return false
    }
}