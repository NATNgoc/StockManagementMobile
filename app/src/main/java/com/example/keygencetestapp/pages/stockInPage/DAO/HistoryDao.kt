package com.example.keygencetestapp.pages.stockInPage.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.StockInHistory
import kotlinx.coroutines.flow.Flow

data class HistoryWithProduct(
    @Embedded val history: StockInHistory,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: Product
)

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: StockInHistory)



    @Query("SELECT * FROM stock_history ORDER BY created_at DESC")
    fun getAllHistories(): Flow<List<StockInHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(histories: List<StockInHistory>)

    @Query("SELECT * FROM stock_history WHERE product_id = :productId ORDER BY created_at DESC")
    fun getHistoriesByProductId(productId: String): Flow<List<StockInHistory>>

    @Query("""
    SELECT h.*, p.id AS product_id, p.name, p.item_code, p.stock, p.unit, p.created_at AS product_created_at, p.updated_at AS product_updated_at
    FROM stock_history h
    INNER JOIN products p ON h.product_id = p.id
    ORDER BY h.created_at DESC
""")
    fun getAllHistoriesWithProduct(): Flow<List<HistoryWithProduct>>

    @Delete
    suspend fun deleteHistory(history: StockInHistory)
}


//
//enum class UnitTypes(val code: String, val nameType: String) {
//    PIECE("PIECE", "Cái"),
//    KILOGRAM("KG", "Kilogram"),
//    BOX("BOX", "Hộp")
//}
//
//@Entity(
//    tableName = TableNames.PRODUCT_TABLE_NAME,
//    indices = [Index(value = ["item_code"], unique = true)]
//)
//data class Product(
//    @PrimaryKey val id: String = UUID.randomUUID().toString(),
//    val name: String,
//    @ColumnInfo(name = "item_code")
//    val itemCode: String,
//    val stock: Int = 0,
//    val unit: String = UnitTypes.PIECE.code,
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long = System.currentTimeMillis(),
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long? = System.currentTimeMillis()
//)
