package com.example.keygencetestapp.pages.oneCheckScanPage.components

import ScanResult
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.keygencetestapp.R
import com.example.keygencetestapp.components.PortableAlertDialog
import com.example.keygencetestapp.pages.oneCheckScanPage.viewModels.ExportCsvViewModel
import com.example.keygencetestapp.pages.oneCheckScanPage.viewModels.ExportState
import com.example.keygencetestapp.utils.getTimeStamp

@Composable
fun ExportCsvDialogComponent(
    scanResultList: List<ScanResult>,
    exportViewModel: ExportCsvViewModel,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var link by rememberSaveable { mutableStateOf("") }
    var selectedUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()

    ) { uri: Uri? ->
        uri?.let {
            link = it.toString()
            selectedUri = it
        }
    }
    val appName = stringResource(id = R.string.app_name)
    val exportState by exportViewModel.exportState.collectAsState()

    when (exportState) {
        is ExportState.Idle -> {
            // Hiển thị UI mặc định hoặc không làm gì
        }
        is ExportState.Exporting -> {
            LoadingExportDialog(
                onConfirmation = {

                },
                onDismissDialog = {

                }
            )
        }
        is ExportState.Success -> {
            onDismissRequest()
            val message = (exportState as ExportState.Success).message.toString()
            // Hiển thị thông báo thành công, ví dụ:
            exportViewModel.resetExportState()
            Toast.makeText(context, "Export thành công", Toast.LENGTH_SHORT).show()
        }
        is ExportState.Error -> {
            val error = (exportState as ExportState.Error).error

        }
    }

    PortableAlertDialog(
        icon = ImageVector.vectorResource(R.drawable.csv_icon),
        dialogText = if (link.isEmpty()) stringResource(R.string.Dialog_Message) else stringResource(
            R.string.Dialog_Message_Export
        ),
        dialogTitle = stringResource(R.string.Dialog_Tittle_Construct),
        onDismissRequest = {
            onDismissRequest()
        },
        onConfirmation = {
            selectedUri?.let { uri ->
                // Tạo tệp mới trong thư mục được chọn
                val pickedDir = DocumentFile.fromTreeUri(context, uri)
                val currentTimeStamp = getTimeStamp()
                val newFile = pickedDir?.createFile("text/csv", "${currentTimeStamp}_${appName}_exported.csv")
                newFile?.uri?.let { fileUri ->
                    exportViewModel.exportResultsToCsv(scanResultList, fileUri, context)
                }
            }
        },
        dialogContent = {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                if (link.isNotEmpty()) {
                    OutlinedCard(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = link,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    launcher.launch(null)
                }) {
                    Text("Chọn thư mục lưu")

                }
            }
        }
    )
}

//@Preview
//@Composable
//fun ExportCsvComponentPreview() {
//    Surface {
//        ExportCsvDialogComponent()
//    }
//}