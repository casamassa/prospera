package com.casamassa.prospera.domain.use_case

import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.TransactionRepository

class InsertTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: FinancialTransaction): Result<Long> {
        return try {
            val account = accountRepository.getAccountById(transaction.contaId)
                ?: return Result.failure(Exception("Conta não encontrada"))

            val newBalance = if (transaction.tipo == TransactionType.RECEITA) {
                account.saldoAtual + transaction.valor
            } else {
                account.saldoAtual - transaction.valor
            }

            // In a real scenario, this should be an atomic transaction
            val id = transactionRepository.insertTransaction(transaction)
            accountRepository.updateAccountBalance(transaction.contaId, newBalance)
            
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
