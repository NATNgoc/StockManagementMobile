package com.example.keygencetestapp.database

import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.keygencetestapp.database.DAO.ParameterDao
import com.example.keygencetestapp.database.entities.Parameters
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryDao
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.database.entities.PurchaseOrderItem
import com.example.keygencetestapp.database.entities.StockInHistory

@Database(
    entities = [Product::class, StockInHistory::class, ProcessOrder::class, PurchaseOrderItem::class, Parameters::class],
    version = 11,
    exportSchema = true
)
abstract class StockDatabase : RoomDatabase() {

    companion object {
        const val NAME = "DatabaseSatoApp"
    }


    abstract fun productDao(): ProductDao
    abstract fun historyDao(): HistoryDao
    abstract fun purchaseOrder(): PurchaseOrderDao
    abstract fun purchaseOrderItem(): PurchaseOrderItemDao
    abstract fun parameterDao(): ParameterDao
}