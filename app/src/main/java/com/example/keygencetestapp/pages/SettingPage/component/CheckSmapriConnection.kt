package com.example.keygencetestapp.pages.SettingPage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import compose.icons.TablerIcons
import compose.icons.tablericons.Printer
import compose.icons.tablericons.Wifi

@Composable
fun PrinterConnectionCheckScreen(
    modifier: Modifier = Modifier,
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    onCheckConnection: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize().padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = TablerIcons.Printer,
            contentDescription = "Printer Icon",
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFEFF3FF), shape = CircleShape)
                .padding(16.dp),
            tint = Color(0xFF2C5AFF)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Check Printer Connection",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter your printer's IP address in SMAPRI to check the connection",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = ipAddress,
            onValueChange = onIpAddressChange,
            label = { Text("Smapri IP Address") },
            placeholder = { Text(ipAddress) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "E.g.: http://192.168.1.100 or https://10.0.0.50",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Button(
            onClick = onCheckConnection,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = TablerIcons.Wifi,
                    contentDescription = "Check Connection",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Check Connection")
            }
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = "Note:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• Make sure the printer is powered on and connected to the network\n" +
                        "• Ensure your device and the printer are on the same network\n" +
                        "• The IP address of Smapri must be in the correct format\n" +
                        "• The Smapri server is already turned on\n",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun PreviewCheck(modifier: Modifier = Modifier) {
    var ipAddress by remember {
        mutableStateOf("192.168.0.1")
    }
    Surface {
        PrinterConnectionCheckScreen(
            ipAddress = ipAddress,
            onIpAddressChange = { ipAddress = it },
            onCheckConnection = { TODO() }
        )
    }
}