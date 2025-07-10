package com.example.keygencetestapp.pages.HomePage.constant

import androidx.annotation.DrawableRes
import com.example.keygencetestapp.R
import com.example.keygencetestapp.constant.Routes

enum class FeatureListConstant(
    val id: Int,
    val nameFeature: String,
    val desFeature: String,
    @DrawableRes val imageId: Int,
    val route: String,
) {
    ONE_CHECK_FEATURE(1, "One-Check", "The One-Check feature allows for quick and easy verification with a single tap.", R.drawable.one_check_lable, Routes.ONE_CHECK_SCREEN),
    STOCK_IN(2, "Stock-In", "The Stock-In feature allows for quick and easy verification with a single tap.", R.drawable.stock_in, Routes.STOCK_IN_SCREEN),
    STOCK_OUT(3, "Stock-Out", "The Stock-Out feature allows for quick and easy verification with a single tap.", R.drawable.stock_out, Routes.STOCK_OUT_SCREEN),
    HISTORY_INVENTORY(4, "History-Inventory", "The History-Inventory feature allows for quick and easy verification with a single tap.", R.drawable.stock_history, Routes.HISTORY_INVENTORY)
}