package com.example.keygencetestapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.MoodEmpty

@Composable
@Preview
fun PlaceHolderScreen(modifier: Modifier = Modifier, icon: @Composable () -> Unit = {
    Icon(TablerIcons.MoodEmpty, null, modifier = Modifier.size(50.dp))
}, text: String = "Please contact later") {
    Surface(modifier.fillMaxSize()) {
        Box {
            Column(Modifier.align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                icon()
                Spacer(modifier.height(10.dp))
                Text(text, fontWeight = FontWeight.Bold)
            }
        }
    }
}