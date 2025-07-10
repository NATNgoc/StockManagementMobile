package com.example.keygencetestapp.DI

import android.app.Application
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderDao
import android.content.Context
import androidx.room.Room
import com.example.keygencetestapp.database.DAO.ParameterDao
import com.example.keygencetestapp.database.StockDatabase
import com.example.keygencetestapp.database.migration.MIGRATION_1_2
import com.example.keygencetestapp.pages.stockInPage.DAO.HistoryDao
import com.example.keygencetestapp.pages.stockInPage.DAO.ProductDao
import com.example.keygencetestapp.pages.stockInPage.DAO.PurchaseOrderItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): StockDatabase {
        return Room.databaseBuilder(
            app,
            StockDatabase::class.java,
            StockDatabase.NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun providePurchaseOrderDao(db: StockDatabase): PurchaseOrderDao {
        return db.purchaseOrder()
    }

    @Provides
    fun providePurchaseOrderItemDao(db: StockDatabase): PurchaseOrderItemDao {
        return db.purchaseOrderItem()
    }

    @Provides
    fun provideProductDao(db: StockDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    fun provideHistoryDao(db: StockDatabase): HistoryDao {
        return db.historyDao()
    }

    @Provides
    fun provideParameterDao(db: StockDatabase): ParameterDao {
        return db.parameterDao()
    }
}