package com.casamassa.prospera.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contas")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "saldo_inicial") val saldoInicial: Double,
    @ColumnInfo(name = "saldo_atual") val saldoAtual: Double,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)
