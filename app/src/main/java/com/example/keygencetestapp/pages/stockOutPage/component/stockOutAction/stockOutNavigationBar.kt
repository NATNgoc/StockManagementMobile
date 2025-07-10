package com.example.keygencetestapp.pages.stockOutPage.component.stockOutAction

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.keygencetestapp.R

@Composable
fun StockOutActionNavigationBar(selectedItem: Int = 0, onItemChange: (Int) -> Unit = {}) {
    val items = listOf("General", "Items")
    val unselectedIcons= listOf(
        ImageVector.vectorResource(R.drawable.browse_activity), ImageVector.vectorResource(
            R.drawable.items))
    val selectedIcons =
        listOf(ImageVector.vectorResource(R.drawable.browse_activity_24dp_filled), ImageVector.vectorResource(R.drawable.items_filled))

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