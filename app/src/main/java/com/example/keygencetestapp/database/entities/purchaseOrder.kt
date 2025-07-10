package com.example.keygencetestapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keygencetestapp.pages.stockInPage.constant.TableNames
import com.example.keygencetestapp.utils.generateUnique20DigitId
import java.util.UUID

enum class ProcessOrderStatus {
    NEW,
    COMPLETED
}

enum class ProcessOrderType {
    STOCK_IN,
    STOCK_OUT,
    STOCK_TAKE
}

@Entity(tableName = TableNames.PROCESS_ORDER)
data class ProcessOrder(
    @PrimaryKey val id: String = generateUnique20DigitId(),
    val note: String,
    val status: String = ProcessOrderStatus.NEW.name,
    val type: String = ProcessOrderType.STOCK_IN.name,
    @ColumnInfo(name = "start_at")
    val planStartDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_at")
    val planEndDate: Long = System.currentTimeMillis().plus(1000 * 60 * 60 * 24 * 2),
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)


