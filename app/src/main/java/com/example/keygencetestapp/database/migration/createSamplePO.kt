package com.example.keygencetestapp.database.migration


import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.keygencetestapp.pages.stockInPage.constant.TableNames


val MIGRATION_1_2 = object : Migration(1, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `${TableNames.PROCESS_ORDER}` (
                `id` TEXT NOT NULL,
                `note` TEXT NOT NULL,
                `status` TEXT NOT NULL,
                `created_at` INTEGER NOT NULL,
                `updated_at` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO `${TableNames.PROCESS_ORDER}` 
            VALUES (
                'a5f08f68-a581-4fe8-8dc7-e452177c1da1',
                'Sample purchase order',
                'PENDING',
                ${System.currentTimeMillis()},
                ${System.currentTimeMillis()}
            )
        """.trimIndent())
    }
}