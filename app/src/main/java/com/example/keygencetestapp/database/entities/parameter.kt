package com.example.keygencetestapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keygencetestapp.utils.generateUnique20DigitId


@Entity(
    tableName = "parameters",
    indices = [androidx.room.Index(value = ["key"], unique = true)]
)
data class Parameters(
    @PrimaryKey
    val id: String = generateUnique20DigitId(),

    val key: String,

    val value: String,
)