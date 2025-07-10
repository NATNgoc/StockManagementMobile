package com.example.keygencetestapp.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.keygencetestapp.database.entities.Parameters
import com.example.keygencetestapp.database.entities.StockInHistory

@Dao
interface ParameterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParameter(parameter: Parameters)

    @Query("SELECT * FROM parameters where `key` = :key")
    suspend fun getValueByKey(key: String): Parameters?

    @Query("UPDATE parameters SET value = :value WHERE `key` = :Key")
    suspend fun updateValueByKey(Key: String, value: String)

}