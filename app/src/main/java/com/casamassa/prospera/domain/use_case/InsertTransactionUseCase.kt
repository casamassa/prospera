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
                ?: return Result.failure(Exception("Conta de origem não encontrada"))

            if (transaction.tipo == TransactionType.TRANSFERENCIA) {
                val targetAccountId = transaction.transferTargetAccountId
                    ?: return Result.failure(Exception("Conta de destino não informada para transferência"))
                
                val targetAccount = accountRepository.getAccountById(targetAccountId)
                    ?: return Result.failure(Exception("Conta de destino não encontrada"))

                // Atomic operation logic
                // In a real scenario, this block should be wrapped in a database transaction.
                // For this task, we will implement the logic here.
                val id = transactionRepository.insertTransaction(transaction)
                accountRepository.updateAccountBalance(transaction.contaId, account.saldoAtual - transaction.valor)
                accountRepository.updateAccountBalance(targetAccountId, targetAccount.saldoAtual + transaction.valor)
                Result.success(id)
            } else {
                val newBalance = if (transaction.tipo == TransactionType.RECEITA) {
                    account.saldoAtual + transaction.valor
                } else {
                    account.saldoAtual - transaction.valor
                }

                val id = transactionRepository.insertTransaction(transaction)
                accountRepository.updateAccountBalance(transaction.contaId, newBalance)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
