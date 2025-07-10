package com.example.keygencetestapp.pages.HomePage.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowHeightSizeClass
import coil3.compose.AsyncImage
import com.example.keygencetestapp.R
import com.example.keygencetestapp.pages.HomePage.constant.FeatureListConstant
import com.example.keygencetestapp.ui.theme.KeyGenceTestAppTheme


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FeatureSelectionList(navController: NavController, modifier: Modifier = Modifier) {
    val listItems = FeatureListConstant.entries
    val scrollState = rememberScrollState()

    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.maxValue == 0) 0f
            else scrollState.value.toFloat() / scrollState.maxValue
        }
    }
    val animatedProgress by animateFloatAsState(targetValue = scrollProgress)

    Column(modifier) {

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .weight(0.9f).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            listItems.forEach { item ->
                FeatureSelection(
                    featureItemData = item,
                    onClickFeature = {
                        navController.navigate(item.route)
                    }
                )
            }
        }
        Box(Modifier.weight(0.1f)) {
            LinearWavyProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                     // Thêm chiều cao rõ ràng
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center)
            )
        }
//        Spacer(Modifier.height(8.dp))



    }
}

@Preview
@Composable
fun FeatureSelectionListPreview() {
    KeyGenceTestAppTheme {
        Surface {
//            FeatureSelectionList()
        }
    }
}

@Composable
fun FeatureSelection(
    onClickFeature: () -> Unit,
    featureItemData: FeatureListConstant,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val widthDpPerCard = 250.dp
    val isCheck = true
    ElevatedCard(
        modifier = modifier
            .width(widthDpPerCard)
            .fillMaxHeight(),
        shape = CircleShape,

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                alignment = Alignment.Top,
                space = 10.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = featureItemData.imageId, contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = featureItemData.nameFeature,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
                    Text(
                        text = featureItemData.desFeature,
                        modifier = Modifier.padding(horizontal = 15.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
                Spacer(modifier = Modifier.weight(0.7f))
            }
            Button(
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    onClickFeature()
                },
                shape = CircleShape,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.play_around_icon),
                        contentDescription = null,
                        tint = if (isCheck) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }
}

@Composable
fun CustomRadioButton(
    isCheck: Boolean = false,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier) {

    }
}

@Preview
@Composable
fun FeatureSelectionPreview() {
    KeyGenceTestAppTheme {
        Surface {
//            FeatureSelection(FeatureListConstant.ONE_CHECK_FEATURE)
        }
    }
}

@Preview
@Composable
fun CustomRadioButtonPreview() {
    KeyGenceTestAppTheme {
        Surface {
            CustomRadioButton(false, {})
        }
    }
}