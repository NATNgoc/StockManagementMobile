package com.example.keygencetestapp.pages.stockInPage.constant

import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.utils.formatTimestamp

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import java.util.UUID
//
//@Entity(tableName = TableNames.PURCHASE_ORDER)
//data class PurchaseOrder(
//    @PrimaryKey val id: String = UUID.randomUUID().toString(),
//    val note: String,
//    val status: String,
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long = System.currentTimeMillis(),
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long
//)

object DefaultPurchaseOrder {
    const val ID = "17509875762994232288"
    const val NOTE = "Sample PO for testing purpose"
    val STATUS = ProcessOrderStatus.COMPLETED.name
    const val CREATED_AT = "1749006852359"
    const val UPDATED_AT = "1749006852359"

    val asMap: Map<String, Any> = mapOf(
        "ID" to ID,
        "NOTE" to NOTE,
        "STATUS" to STATUS,
        "CREATED_AT" to formatTimestamp(CREATED_AT),
        "UPDATED_AT" to formatTimestamp(UPDATED_AT)
    )
}