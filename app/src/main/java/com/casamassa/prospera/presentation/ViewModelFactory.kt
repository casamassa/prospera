package com.casamassa.prospera.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.CategoryRepository
import com.casamassa.prospera.domain.repository.TransactionRepository
import com.casamassa.prospera.domain.use_case.InsertTransactionUseCase
import com.casamassa.prospera.presentation.categorias.CategoriasViewModel
import com.casamassa.prospera.presentation.contas.ContasViewModel
import com.casamassa.prospera.presentation.fluxo.FluxoViewModel
import com.casamassa.prospera.presentation.home.HomeViewModel
import com.casamassa.prospera.presentation.lancamento.LancamentoViewModel

class ViewModelFactory(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val insertTransactionUseCase: InsertTransactionUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(accountRepository) as T
            }
            modelClass.isAssignableFrom(FluxoViewModel::class.java) -> {
                FluxoViewModel(transactionRepository) as T
            }
            modelClass.isAssignableFrom(LancamentoViewModel::class.java) -> {
                LancamentoViewModel(
                    accountRepository = accountRepository,
                    categoryRepository = categoryRepository,
                    insertTransactionUseCase = insertTransactionUseCase
                ) as T
            }
            modelClass.isAssignableFrom(ContasViewModel::class.java) -> {
                ContasViewModel(
                    accountRepository = accountRepository,
                    transactionRepository = transactionRepository
                ) as T
            }
            modelClass.isAssignableFrom(CategoriasViewModel::class.java) -> {
                CategoriasViewModel(categoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
