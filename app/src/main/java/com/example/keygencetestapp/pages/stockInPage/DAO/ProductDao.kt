package com.example.keygencetestapp.pages.stockInPage.DAO

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.keygencetestapp.database.entities.Product
import kotlinx.coroutines.flow.Flow

data class ProductIdWithCount(
    val id: String,
    @ColumnInfo(name = "stock")
    val count: Int
)

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createProduct(product: Product)

    @Query("UPDATE products SET stock = :quantity WHERE id = :id")
    suspend fun updateProductQuantityById(id: String, quantity: Int)

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun findProductById(id: String): Flow<Product?>

    @Query("SELECT * FROM products WHERE item_code = :itemCode COLLATE NOCASE LIMIT 1")
    fun findProductByItemCode(itemCode: String): Flow<Product?>

    @Query("SELECT COUNT(id) FROM products WHERE id IN (:productIds)")
    suspend fun countValidProductIds(productIds: List<String>): Int

    @Query("SELECT * FROM products WHERE item_code LIKE '%' || :query || '%' COLLATE NOCASE OR name LIKE '%' || :query || '%' COLLATE NOCASE")
    fun findProductByNameAndItemCode(query: String): Flow<Product?>

    @Query("SELECT * FROM products")
    fun findAllProduct(): Flow<List<Product>>

    @Query("SELECT id, stock FROM products WHERE id IN (:ids)")
    fun findProductWithIdsAndCount(ids: List<String>): Flow<List<ProductIdWithCount>>

    @Update
    suspend fun updateProducts(products: List<Product>)
}