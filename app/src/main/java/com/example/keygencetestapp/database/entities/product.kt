package com.example.keygencetestapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.keygencetestapp.pages.stockInPage.constant.TableNames
import com.example.keygencetestapp.utils.generateUnique20DigitId
import java.util.UUID




enum class UnitTypes(val code: String, val nameType: String) {
    PIECE("PIECE", "Cái"),
    KILOGRAM("KG", "Kilogram"),
    BOX("BOX", "Hộp")
}

@Entity(
    tableName = TableNames.PRODUCT_TABLE_NAME,
    indices = [Index(value = ["item_code"], unique = true)]
)
data class Product(
    @PrimaryKey val id: String = generateUnique20DigitId(),
    val name: String,
    @ColumnInfo(name = "item_code")
    val itemCode: String,
    val stock: Int = 0,
    val unit: String = UnitTypes.PIECE.code,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long? = System.currentTimeMillis()
)
