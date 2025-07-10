package com.example.keygencetestapp.pages.SettingPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.pages.SettingPage.component.PrinterConnectionCheckScreen
import com.example.keygencetestapp.pages.SettingPage.viewModel.SettingViewModel
import com.example.keygencetestapp.utils.ResponseState
import compose.icons.TablerIcons
import compose.icons.tablericons.MoodSad
import com.example.keygencetestapp.components.PortableAlertDialog
import compose.icons.tablericons.CircleCheck

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingPage(modifier: Modifier = Modifier, viewModel: SettingViewModel = hiltViewModel()) {
    var ip by remember {
        mutableStateOf("")
    }
    val curIpAddress = viewModel.initialIpAddressState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getIpAddress()
    }
    val checkConnectionState = viewModel.checkConnectionState.collectAsState()

    Surface(modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                )
                .alpha(if (checkConnectionState.value is ResponseState.Loading<*> || curIpAddress.value is ResponseState.Loading<*>) 0.5f else 1f),
        ) {
            DisplayDialogByState(
                checkConnectionState.value,
                Modifier
                    .align(Alignment.Center)
                    .zIndex(1F)
            ) {
                viewModel.refreshState()
            }
            when(curIpAddress.value) {
                is ResponseState.Loading<*> -> {
                    ContainedLoadingIndicator(Modifier.align(Alignment.Center))                }
                is ResponseState.Success<*> -> {
                    if (ip.isEmpty()) {
                        ip = (curIpAddress.value as ResponseState.Success<String>).data
                    }
                    PrinterConnectionCheckScreen(
                        Modifier
                            .fillMaxSize()
                            .then(
                                if (checkConnectionState.value is ResponseState.Loading<*>) Modifier.zIndex(
                                    -1F
                                ) else Modifier
                            ).clickable(
                                enabled = checkConnectionState.value !is ResponseState.Loading<*>,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {}
                            ),
                        ipAddress = ip,
                        onIpAddressChange = { ip = it },
                        onCheckConnection = {
                            viewModel.checkConnection(ip)
                        },
                    )
                }
                else -> {

                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun DisplayDialogByState(
    checkConnectionState: ResponseState<Boolean>,
    modifier: Modifier = Modifier,
    onRefreshState: () -> Unit,
) {
    var isShowDialog by remember {
        mutableStateOf(false)
    }
    if (isShowDialog) {
        PortableAlertDialog(
            onDismissRequest = {
                isShowDialog = false
                onRefreshState()
            },
            onConfirmation = {
                isShowDialog = false
                onRefreshState()
            },
            dialogTitle = if (checkConnectionState is ResponseState.Error) "Failure" else "Success",
            dialogText = if (checkConnectionState is ResponseState.Error) "Please try again" else "Success connect to the printer!",
            dialogContent = {
                if (checkConnectionState is ResponseState.Error<*>) {
                    Text(text = (checkConnectionState as ResponseState.Error<*>).message.toString())
                }
            },
            icon = if (checkConnectionState is ResponseState.Error<*>) TablerIcons.MoodSad else TablerIcons.CircleCheck

        )
    }
    Box(modifier) {
        when (checkConnectionState) {
            is ResponseState.Loading<*> -> {
                ContainedLoadingIndicator(Modifier.align(Alignment.Center))
            }

            is ResponseState.Error<*> -> {
                isShowDialog = true
            }

            is ResponseState.Idle<*> -> {

            }

            is ResponseState.Success<*> -> {
                isShowDialog = true
            }
        }
    }
}