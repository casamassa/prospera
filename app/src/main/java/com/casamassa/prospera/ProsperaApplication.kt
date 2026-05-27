package com.casamassa.prospera

import android.app.Application
import androidx.room.Room
import com.casamassa.prospera.data.local.database.AppDatabase
import com.casamassa.prospera.data.repository.AccountRepositoryImpl
import com.casamassa.prospera.data.repository.CategoryRepositoryImpl
import com.casamassa.prospera.data.repository.TransactionRepositoryImpl
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.CategoryRepository
import com.casamassa.prospera.domain.repository.TransactionRepository

class ProsperaApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var accountRepository: AccountRepository
        private set

    lateinit var categoryRepository: CategoryRepository
        private set

    lateinit var transactionRepository: TransactionRepository
        private set

    override fun onCreate() {
        super.onCreate()
        
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "prospera_db"
        ).build()

        accountRepository = AccountRepositoryImpl(database.accountDao())
        categoryRepository = CategoryRepositoryImpl(database.categoryDao())
        transactionRepository = TransactionRepositoryImpl(database.transactionDao())
    }
}
