package com.example.keygencetestapp.pages.stockInPage

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keygencetestapp.R
import com.example.keygencetestapp.pages.stockInPage.components.ImportOptionList
import com.example.keygencetestapp.pages.stockInPage.constant.DefaultPurchaseOrder
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.pages.stockInPage.viewModel.POViewModel
import com.example.keygencetestapp.utils.ResponseState


@Composable
fun StockInPage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Surface {
        InitScreen(navController = navController)
    }
}

@Composable
fun InitScreen(
    modifier: Modifier = Modifier,
    poViewModel: POViewModel = hiltViewModel(),
    navController: NavController
) {
    val order: ResponseState<ProcessOrder?> by poViewModel.defaultPO.collectAsState()

    LaunchedEffect(Unit) {
        poViewModel.getDefaultPurchaseOrder2()
    }

    Log.v("Init Screen", order.toString())
    Surface(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            when (order) {

                is ResponseState.Success -> {
                    Column {
                        DefaultOrderScreen()
                        ImportOptionList(navController = navController)
                    }
                }

                is ResponseState.Error -> {
                    Button(
                        onClick = { poViewModel.insertStockInPO() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Default Sample Order")
                    }
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DefaultOrderScreen(modifier: Modifier = Modifier) {
    Surface(modifier) {
        Card(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                DefaultPurchaseOrder.asMap.forEach {
                    if (it.key != "ID" && it.key != "NOTE") {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.index_right),
                                contentDescription = null
                            )
                            Text(
                                text = "${it.key.lowercase()}: ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(text = "${it.value}", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else if (it.key == "NOTE") {
                        Text(
                            text = "${it.value}",
                            style = MaterialTheme.typography.titleSmall,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.ExtraBold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                    } else {
                        Text(
                            text = "ID: ${it.value}",
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.ExtraBold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewDefaultOrderScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
    ) {
        DefaultOrderScreen()
    }
}