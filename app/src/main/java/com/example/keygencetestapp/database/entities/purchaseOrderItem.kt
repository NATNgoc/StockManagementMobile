package com.example.keygencetestapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.keygencetestapp.pages.stockInPage.constant.TableNames
import com.example.keygencetestapp.utils.generateUnique20DigitId
import java.util.UUID


@Entity(
    tableName = TableNames.PROCESS_ORDER_ITEM,
    foreignKeys = [
        ForeignKey(
            entity = ProcessOrder::class,
            parentColumns = ["id"],
            childColumns = ["process_order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseOrderItem(
    @PrimaryKey val id: String = generateUnique20DigitId(),

    @ColumnInfo(name = "process_order_id")
    val processOrderId: String,

    @ColumnInfo(name = "product_id")
    val productId: String,

    val quantity: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)