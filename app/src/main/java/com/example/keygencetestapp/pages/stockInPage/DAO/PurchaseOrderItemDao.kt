package com.example.keygencetestapp.pages.stockInPage.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.keygencetestapp.database.entities.PurchaseOrderItem

@Dao
interface PurchaseOrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseOrderItems(purchaseOrderItems: List<PurchaseOrderItem>)

}