package com.casamassa.prospera.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val totalBalance: Double = 0.0,
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = accountRepository.getAllActiveAccounts()
        .combine(MutableStateFlow(false)) { accounts, loading ->
            // Se as contas estiverem vazias, podemos estar no primeiro acesso
            if (accounts.isEmpty()) {
                initializeDefaultAccount()
            }
            
            HomeUiState(
                totalBalance = accounts.sumOf { it.saldoAtual },
                accounts = accounts,
                isLoading = loading
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    private fun initializeDefaultAccount() {
        viewModelScope.launch {
            // Check again inside coroutine to avoid race conditions
            // Note: In a robust app, this would be in a Splash/Start Use Case
            // but for Task 012 requirement we do it here if empty.
            accountRepository.insertAccount(
                Account(
                    nome = "Carteira",
                    saldoInicial = 0.0,
                    saldoAtual = 0.0
                )
            )
        }
    }
}
