package com.casamassa.prospera.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.casamassa.prospera.data.local.dao.AccountDao
import com.casamassa.prospera.data.local.dao.CategoryDao
import com.casamassa.prospera.data.local.dao.TransactionDao
import com.casamassa.prospera.data.local.entities.AccountEntity
import com.casamassa.prospera.data.local.entities.CategoryEntity
import com.casamassa.prospera.data.local.entities.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
