package com.example.keygencetestapp.pages.stockOutPage.type

data class StockOutActionProgress(
    var totalScanned: Int = 0,
    var remain: Int = 0,
    var required: Int = 0,
    var listItemProgress: MutableList<ItemStockOutDetailProgress> = arrayListOf()
)

data class ItemStockOutDetailProgress(
    var id: String = "",
    var name: String = "",
    var itemCode: String = "",
    var scanned: Int = 0,
    var required: Int = 0,
    var remain: Int = 0,
)