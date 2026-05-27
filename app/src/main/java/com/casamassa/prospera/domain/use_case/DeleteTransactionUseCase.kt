package com.casamassa.prospera.domain.use_case

import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: FinancialTransaction): Result<Unit> {
        return try {
            val account = accountRepository.getAccountById(transaction.contaId)
                ?: return Result.failure(Exception("Conta não encontrada"))

            // Inverse logic for deleting
            val newBalance = if (transaction.tipo == TransactionType.RECEITA) {
                account.saldoAtual - transaction.valor
            } else {
                account.saldoAtual + transaction.valor
            }

            transactionRepository.deleteTransaction(transaction)
            accountRepository.updateAccountBalance(transaction.contaId, newBalance)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
