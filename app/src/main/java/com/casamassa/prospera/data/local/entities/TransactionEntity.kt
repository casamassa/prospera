package com.casamassa.prospera.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "lancamentos",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["conta_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["transfer_target_account_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "descricao") val descricao: String,
    @ColumnInfo(name = "valor") val valor: Double,
    @ColumnInfo(name = "data_timestamp") val dataTimestamp: Long,
    @ColumnInfo(name = "tipo") val tipo: String, // "RECEITA" ou "DESPESA"
    @ColumnInfo(name = "conta_id") val contaId: Long,
    @ColumnInfo(name = "categoria_id") val categoriaId: Long,
    @ColumnInfo(name = "transfer_target_account_id") val transferTargetAccountId: Long? = null
)
