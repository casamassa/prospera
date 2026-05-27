package com.casamassa.prospera.domain.model

data class FinancialTransaction(
    val id: Long = 0,
    val descricao: String,
    val valor: Double,
    val dataTimestamp: Long,
    val tipo: TransactionType,
    val contaId: Long,
    val categoriaId: Long,
    val transferTargetAccountId: Long? = null
)
