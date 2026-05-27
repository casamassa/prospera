package com.casamassa.prospera.presentation.home

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.domain.model.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
            Account(id = 1, nome = "Carteira", saldoInicial = 150.50, saldoAtual = 150.50),
            Account(id = 2, nome = "Banco Inter", saldoInicial = 2500.00, saldoAtual = 2500.00),
            Account(id = 3, nome = "Nubank", saldoInicial = 1200.75, saldoAtual = 1200.75),
            Account(id = 4, nome = "Poupança", saldoInicial = 5000.00, saldoAtual = 5000.00)
        )
        val total = mockAccounts.sumOf { it.saldoAtual }
        
        _uiState.value = HomeUiState(
            totalBalance = total,
            accounts = mockAccounts
        )
    }
}
