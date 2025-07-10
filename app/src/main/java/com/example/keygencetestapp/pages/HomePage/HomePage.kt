package com.example.keygencetestapp.pages.HomePage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.example.keygencetestapp.R
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.pages.HomePage.components.FeatureSelectionList
import com.example.keygencetestapp.ui.theme.KeyGenceTestAppTheme

@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    KeyGenceTestAppTheme {
        Surface(modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().weight(0.1f)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.SETTING_SCREEN)
                        },
                    ) {
                        Icon(painter = painterResource(R.drawable.settings_24dp), null)
                    }
                }
                Box(Modifier
                    .fillMaxWidth()
                    .weight(0.9f)) {
                    FeatureSelectionList(navController, Modifier.fillMaxSize())
                }
            }
        }
    }
}