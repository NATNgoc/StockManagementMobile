package com.example.keygencetestapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.keygencetestapp.pages.stockInPage.constant.TableNames
import com.example.keygencetestapp.utils.generateUnique20DigitId
import java.util.UUID



enum class StockType {
    STOCK_IN, STOCK_OUT
}

@Entity(
    tableName = TableNames.HISTORY_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["id"],
        childColumns = ["product_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["product_id"])]
)
data class StockInHistory(
    @PrimaryKey
    val id: String = generateUnique20DigitId(),
    @ColumnInfo(name = "product_id")
    val productId: String,
    val quantity: Int,
    @ColumnInfo(name = "total_in_inventory")
    val totalQuantityInInventory: Int,
    val note: String? = "",
    val type: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long? = System.currentTimeMillis()
)