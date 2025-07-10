package com.example.keygencetestapp.pages.stockOutPage.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keygencetestapp.R
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowUp
import compose.icons.tablericons.DotsVertical
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Trash

@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverflowingHorizontalFloatingToolbar(
    listState: LazyListState,
    modifier: Modifier,
    onPlusClick: () -> Unit = {},
    onConfirm: () -> Unit = {},
    isEnableSubmit: Boolean
) {
    var toolbarVisible by remember { mutableStateOf(true) }
    var previousIndex by remember { androidx.compose.runtime.mutableIntStateOf(0) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }


    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset

        val isScrollingDown = when {
            currentIndex > previousIndex -> true
            currentIndex == previousIndex && currentOffset > previousScrollOffset -> true
            else -> false
        }

        toolbarVisible = !isScrollingDown

        previousIndex = currentIndex
        previousScrollOffset = currentOffset
    }
    AnimatedVisibility(
        visible = toolbarVisible,
        modifier = modifier.offset(y = -ScreenOffset),
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        HorizontalFloatingToolbar(
            expanded = true,
            trailingContent = {
                IconButton(
                    modifier = Modifier.width(100.dp),
                    onClick = {
                        onPlusClick()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.box_add),
                        contentDescription = "Localized description"
                    )
                }
            },
            leadingContent = {
                IconButton(onClick = {}, modifier = Modifier.width(100.dp)) {

                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = TablerIcons.Trash,
                        contentDescription = null
                    )

                }
            },
            content = {
                FilledIconButton(
                    modifier = Modifier.width(100.dp),
                    onClick = { onConfirm() },
                    enabled = isEnableSubmit
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.deployed_code_update),
                        contentDescription = "Localized description"
                    )
                }
            }

        )
    }

}