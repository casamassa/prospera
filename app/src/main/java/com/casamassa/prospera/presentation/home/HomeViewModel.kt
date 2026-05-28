package com.casamassa.prospera.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.repository.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val totalBalance: Double = 0.0,
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = accountRepository.getAllActiveAccounts()
        .map { accounts ->
            HomeUiState(
                totalBalance = accounts.sumOf { it.saldoAtual },
                accounts = accounts,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
}
