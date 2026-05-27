package com.casamassa.prospera.domain.model

data class Account(
    val id: Long = 0,
    val nome: String,
    val saldoInicial: Double,
    val saldoAtual: Double,
    val isActive: Boolean = true
)
