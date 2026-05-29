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
            // Revert balance using the same logic as InsertTransactionUseCase
            revertBalance(transaction)
            
            // Delete record
            transactionRepository.deleteTransaction(transaction)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun revertBalance(transaction: FinancialTransaction) {
        if (transaction.tipo == TransactionType.TRANSFERENCIA) {
            val sourceAccount = accountRepository.getAccountById(transaction.contaId)
            val targetAccountId = transaction.transferTargetAccountId
            if (sourceAccount != null && targetAccountId != null) {
                val targetAccount = accountRepository.getAccountById(targetAccountId)
                if (targetAccount != null) {
                    // Revert transfer: Add back to source, take from target
                    accountRepository.updateAccountBalance(transaction.contaId, sourceAccount.saldoAtual + transaction.valor)
                    accountRepository.updateAccountBalance(targetAccountId, targetAccount.saldoAtual - transaction.valor)
                }
            }
        } else {
            val account = accountRepository.getAccountById(transaction.contaId)
            if (account != null) {
                // Revert: If was revenue, subtract. If was expense, add.
                val revertedBalance = if (transaction.tipo == TransactionType.RECEITA) {
                    account.saldoAtual - transaction.valor
                } else {
                    account.saldoAtual + transaction.valor
                }
                accountRepository.updateAccountBalance(transaction.contaId, revertedBalance)
            }
        }
    }
}
