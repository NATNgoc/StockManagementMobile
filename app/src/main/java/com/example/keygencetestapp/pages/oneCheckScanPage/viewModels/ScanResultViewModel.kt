//package com.example.keygencetestapp.ui.theme.pages.viewModels
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.keygencetestapp.pages.oneCheckScanPage.constant.ScanConstant
import com.keyence.autoid.sdk.notification.Notification
import com.keyence.autoid.sdk.scan.ScanManager
import com.keyence.autoid.sdk.scan.scanparams.UserFeedback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


data class ScanResult(
    val id: Int,
    val value: String,
    val result: Boolean,
)


//@HiltViewModel
//class ScanViewModel @Inject constructor() : ViewModel() {

class ScanViewModel : ViewModel() {

    private val _scanResults = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanResults: StateFlow<List<ScanResult>> get() = _scanResults
    private var currentId = 0

//    private var scanManager: ScanManager? = null
//    private var notification: Notification? = null
//
//    fun initScanManagerAndNotification(context: Context) {
//        scanManager = ScanManager.createScanManager(context)
//        notification = Notification.createNotification(context)
//    }

    fun addResult(value: String, result: Boolean, isShowDialog: Boolean? = null) {
        val newResult = ScanResult(
            id = currentId++,
            value = value,
            result = result
        )
        _scanResults.update { currentList ->
            currentList + newResult
        }
    }

    fun getList(): List<ScanResult> = _scanResults.value

//    fun triggerErrorVibration() {
//        scanManager?.let {
//            val userFeedbackParams = UserFeedback()
//            val sdkStatusSet = it.getConfig(userFeedbackParams)
//            userFeedbackParams.error.vibrator = true
//            it.setConfig(userFeedbackParams)
//        }
//    }
//
//    fun triggerBuzzer(
//        feedbackParams: ScanConstant.FeedbackParams = ScanConstant.ErrorFeedbackParams()
//    ): Boolean {
//        return notification?.let {
//            it.startBuzzer(feedbackParams.tone, feedbackParams.onPeriod, feedbackParams.offPeriod, feedbackParams.repeatCount)
//        } ?: false
//    }
//
//    fun stopBuzzer() {
//        notification?.let {
//            it.stopBuzzer()
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        // 4. Discard the instances to release the resources when the ViewModel is cleared [10, 17].
//        scanManager?.releaseScanManager()
//        scanManager = null
//
//        notification?.releaseNotification()
//        notification = null
//    }

//    fun getScanManager(): ScanManager? = scanManager

    fun clear() {
        _scanResults.value = emptyList()
        currentId = 0
    }
}

