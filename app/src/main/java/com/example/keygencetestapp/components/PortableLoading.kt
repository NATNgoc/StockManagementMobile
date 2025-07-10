package com.example.keygencetestapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PortableLoading(modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().background(Color.Transparent)) {
            ContainedLoadingIndicator(Modifier.align(Alignment.Center))
        }
    }
}