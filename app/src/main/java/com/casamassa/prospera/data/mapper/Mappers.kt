package com.casamassa.prospera.data.mapper

import com.casamassa.prospera.data.local.entities.AccountEntity
import com.casamassa.prospera.data.local.entities.CategoryEntity
import com.casamassa.prospera.data.local.entities.TransactionEntity
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id,
        nome = nome,
        saldoInicial = saldoInicial,
        saldoAtual = saldoAtual,
        isActive = isActive
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        nome = nome,
        saldoInicial = saldoInicial,
        saldoAtual = saldoAtual,
        isActive = isActive
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        nome = nome,
        tipo = TransactionType.valueOf(tipo),
        parentCategoryId = parentCategoryId
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        nome = nome,
        tipo = tipo.name,
        parentCategoryId = parentCategoryId
    )
}

fun TransactionEntity.toDomain(): FinancialTransaction {
    return FinancialTransaction(
        id = id,
        descricao = descricao,
        valor = valor,
        dataTimestamp = dataTimestamp,
        tipo = TransactionType.valueOf(tipo),
        contaId = contaId,
        categoriaId = categoriaId,
        transferTargetAccountId = transferTargetAccountId
    )
}

fun FinancialTransaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        descricao = descricao,
        valor = valor,
        dataTimestamp = dataTimestamp,
        tipo = tipo.name,
        contaId = contaId,
        categoriaId = categoriaId,
        transferTargetAccountId = transferTargetAccountId
    )
}
