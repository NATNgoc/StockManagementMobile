package com.example.keygencetestapp.pages.stockInPage.DAO

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.example.keygencetestapp.database.entities.ProcessOrder
import com.example.keygencetestapp.database.entities.ProcessOrderStatus
import com.example.keygencetestapp.database.entities.Product
import com.example.keygencetestapp.database.entities.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

//

data class POWithPOItems (
    @Embedded
    val processOrder: ProcessOrder,

    @Relation(
        parentColumn = "id",
        entityColumn = "process_order_id"
    )
    val purchaseOrderItems: List<PurchaseOrderItem>
)

data class FullProcessOrder(
    @Embedded
    val order: ProcessOrder,

    @Relation(
        entity = PurchaseOrderItem::class,
        parentColumn = "id",
        entityColumn = "process_order_id"
    )
    val items: List<POItemWithProduct>
)

data class POItemWithProduct(
    @Embedded
    val item: PurchaseOrderItem,

    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: Product
)


data class POWithTotalItems(
    @Embedded
    val processOrder: ProcessOrder,
    val countItems: Int
)

@Dao
interface PurchaseOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseOrder(order: ProcessOrder)

    @Query("SELECT * FROM process_orders")
    fun getAllOrders(): Flow<List<ProcessOrder>>

    @Query("SELECT COUNT(*) FROM process_orders")
    suspend fun getOrderCount(): Int

    @Query("SELECT * FROM process_orders WHERE id = :id LIMIT 1")
    fun getOrderById(id: String): Flow<ProcessOrder?>

    @Query("""
        SELECT p.*, po.*
        FROM process_orders p
        INNER JOIN process_order_items po 
        ON p.id = po.process_order_id
    """)
    suspend fun getPOwithPOItems(): List<POWithPOItems>

    @Transaction
    @Query("SELECT * FROM process_orders WHERE id = :id")
    suspend fun getPOwithPOItemsAndProductById(id: String): FullProcessOrder?


    @Query("""
        SELECT p.*, COUNT(po.id) as countItems
        FROM process_orders p
        LEFT JOIN process_order_items po 
        ON p.id = po.process_order_id
        GROUP BY p.id
    """)
    suspend fun getPOwithTotalItems(): List<POWithTotalItems>

    @Query("UPDATE process_orders SET status = :status WHERE id = :id")
    suspend fun updatePOStatus(id: String, status: ProcessOrderStatus)
}