package com.casamassa.prospera.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.casamassa.prospera.data.local.dao.AccountDao
import com.casamassa.prospera.data.local.dao.CategoryDao
import com.casamassa.prospera.data.local.dao.TransactionDao
import com.casamassa.prospera.data.local.entities.AccountEntity
import com.casamassa.prospera.data.local.entities.CategoryEntity
import com.casamassa.prospera.data.local.entities.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    companion object {
        fun getDatabaseCallback(scope: CoroutineScope): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // We can't use the DAO directly here easily without a database instance
                    // But we can defer it to the scope
                }
            }
        }

        suspend fun prepopulate(
            accountDao: AccountDao,
            categoryDao: CategoryDao
        ) {
            // Default Account
            accountDao.insert(
                AccountEntity(
                    nome = "Carteira",
                    saldoInicial = 0.0,
                    saldoAtual = 0.0
                )
            )

            // Default Categories - Despesas
            val despesas = listOf(
                "Alimentação", "Transporte", "Moradia", 
                "Lazer", "Saúde", "Educação", "Outros"
            )
            despesas.forEach {
                categoryDao.insert(
                    CategoryEntity(
                        nome = it,
                        tipo = "DESPESA"
                    )
                )
            }

            // Default Categories - Receitas
            val receitas = listOf(
                "Salário", "Extra", "Investimento", "Outros"
            )
            receitas.forEach {
                categoryDao.insert(
                    CategoryEntity(
                        nome = it,
                        tipo = "RECEITA"
                    )
                )
            }
        }
    }
}
