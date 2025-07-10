package com.example.keygencetestapp.pages.stockOutPage.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keygencetestapp.R


@Composable
fun StockOUtNavigationBar(selectedItem: Int = 0, onItemChange: (Int) -> Unit = {}) {
    val items = listOf("Dashboard", "Manage")
    val unselectedIcons= listOf(ImageVector.vectorResource(R.drawable.analystic), ImageVector.vectorResource(R.drawable.calendar_edit))
    val selectedIcons =
        listOf(ImageVector.vectorResource(R.drawable.analytic_filled), ImageVector.vectorResource(R.drawable.calendar_edit_filled))

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { onItemChange(index) }
            )
        }
    }
}