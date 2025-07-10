package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keygencetestapp.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keygencetestapp.pages.stockOutPage.viewModel.CreatePOViewModel
import com.example.keygencetestapp.pages.stockOutPage.viewModel.ProductWithStockOutCount
import com.example.keygencetestapp.pages.stockOutPage.viewModel.SearchAndChooseItemViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.PlayerTrackNext


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchAndAddItem(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    createPOViewModel: CreatePOViewModel = hiltViewModel(),
    searchViewModel: SearchAndChooseItemViewModel = hiltViewModel()
) {
    var step by remember { mutableIntStateOf(0) }
    val isSelectedItem = searchViewModel.selectedProduct.collectAsState()
    val onConfirmProduct = {
        createPOViewModel.addProductToPO(ProductWithStockOutCount(searchViewModel.selectedProduct.value!!, searchViewModel.getNumberStockOut()))
        searchViewModel.decreaseStockQuantity()
        searchViewModel.resetSelectedProduct()
    }
    Surface(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut())
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                modifier = Modifier.weight(1f)
            ) { currentStep ->
                when (currentStep) {
                    0 -> StepChooseItem(Modifier.weight(1f))
                    1 -> StepChooseCountAndConfirm(Modifier.weight(1f))
                    else -> StepConfirmStockOutItem(Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        searchViewModel.resetSelectedProduct()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Column {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.material_symbols_outlined_backlight_high_off),
                            contentDescription = "Next"
                        )
                        Text("Cancel")
                    }
                }
                Button(
                    onClick = {
                        if (step < 2) step++ else {
                            onConfirmProduct()
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(0.35f),
                    enabled = isSelectedItem.value != null
                ) {
                    Column {
                        Icon(TablerIcons.PlayerTrackNext, contentDescription = "Next")
                        Text(if (step < 2) "Next" else "Finish")
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewNumberStep() {
    NumberStep(1, "Choose Item Below")
}



