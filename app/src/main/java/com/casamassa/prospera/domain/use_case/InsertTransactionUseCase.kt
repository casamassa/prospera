package com.casamassa.prospera.domain.use_case

import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.TransactionRepository

/**
 * UseCase to handle both creation and update of transactions.
 * Implements the Balance Engine logic to ensure account balances are correctly updated.
 */
class InsertTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: FinancialTransaction): Result<Long> {
        return try {
            // 1. If editing, revert old transaction effect first
            if (transaction.id != 0L) {
                val oldTransaction = transactionRepository.getTransactionById(transaction.id)
                if (oldTransaction != null) {
                    revertBalance(oldTransaction)
                }
            }

            // 2. Apply new transaction rule
            if (transaction.tipo == TransactionType.TRANSFERENCIA) {
                applyTransfer(transaction)
            } else {
                applyRegularTransaction(transaction)
            }

            // 3. Save to database
            val id = if (transaction.id == 0L) {
                transactionRepository.insertTransaction(transaction)
            } else {
                transactionRepository.updateTransaction(transaction)
                transaction.id
            }
            
            Result.success(id)
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

    private suspend fun applyRegularTransaction(transaction: FinancialTransaction) {
        val account = accountRepository.getAccountById(transaction.contaId)
            ?: throw Exception("Conta não encontrada")
        
        val newBalance = if (transaction.tipo == TransactionType.RECEITA) {
            account.saldoAtual + transaction.valor
        } else {
            account.saldoAtual - transaction.valor
        }
        accountRepository.updateAccountBalance(transaction.contaId, newBalance)
    }

    private suspend fun applyTransfer(transaction: FinancialTransaction) {
        val sourceAccount = accountRepository.getAccountById(transaction.contaId)
            ?: throw Exception("Conta de origem não encontrada")
        
        val targetAccountId = transaction.transferTargetAccountId
            ?: throw Exception("Conta de destino não informada")
            
        val targetAccount = accountRepository.getAccountById(targetAccountId)
            ?: throw Exception("Conta de destino não encontrada")

        // Subtrair da origem, Somar no destino
        accountRepository.updateAccountBalance(transaction.contaId, sourceAccount.saldoAtual - transaction.valor)
        accountRepository.updateAccountBalance(targetAccountId, targetAccount.saldoAtual + transaction.valor)
    }
}
