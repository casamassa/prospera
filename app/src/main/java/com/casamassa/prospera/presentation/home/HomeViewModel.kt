package com.casamassa.prospera.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Account(
    val name: String,
    val balance: Double
)

data class HomeUiState(
    val totalBalance: Double = 0.0,
    val accounts: List<Account> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val mockAccounts = listOf(
            Account("Carteira", 150.50),
            Account("Banco Inter", 2500.00),
            Account("Nubank", 1200.75),
            Account("Poupança", 5000.00)
        )
        val total = mockAccounts.sumOf { it.balance }
        
        _uiState.value = HomeUiState(
            totalBalance = total,
            accounts = mockAccounts
        )
    }
}
