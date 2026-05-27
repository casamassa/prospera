package com.casamassa.prospera.domain.model

data class Category(
    val id: Long = 0,
    val nome: String,
    val tipo: TransactionType,
    val parentCategoryId: Long? = null
)
