package com.example.keygencetestapp.pages.oneCheckScanPage.viewModels

import ScanResult
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.charset.Charset

sealed class ExportState {
    object Idle : ExportState()
    object Exporting : ExportState()
    data class Success(val message: String) : ExportState()
    data class Error(val error: String) : ExportState()
}

class ExportCsvViewModel : ViewModel() {

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState: StateFlow<ExportState> = _exportState

    fun exportResultsToCsv(results: List<ScanResult>, uri: Uri, context: Context) {
        viewModelScope.launch {
            _exportState.value = ExportState.Exporting
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val csv = createCsvRaw(results)
                    outputStream.write(csv.toByteArray(Charset.forName("UTF-8")))
                    _exportState.value = ExportState.Success("Export CSV thành công!")
                } ?: run {
                    _exportState.value =
                        ExportState.Error("Không thể mở OutputStream từ Uri đã cung cấp.")
                }
            } catch (e: Exception) {
                _exportState.value = ExportState.Error("Xuất CSV thất bại: ${e.message}")
            }
        }
    }

    private fun createCsvRaw(results: List<ScanResult>): String {
        val csvHeader = "ID,Value,Result\n"
        val csvBody = results.joinToString("\n") { res ->
            "${res.id},${res.value},${res.result}"
        }
        val csv = csvHeader + csvBody
        return csv
    }

    fun resetExportState() {
        _exportState.value = ExportState.Idle
    }
}
