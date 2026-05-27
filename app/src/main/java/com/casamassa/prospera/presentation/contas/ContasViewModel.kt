package com.casamassa.prospera.presentation.contas

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.domain.model.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ContasUiState(
    val accounts: List<Account> = emptyList(),
    val isDialogVisible: Boolean = false,
    val editingAccount: Account? = null,
    val nameInput: String = "",
    val balanceInput: String = ""
)

class ContasViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContasUiState())
    val uiState: StateFlow<ContasUiState> = _uiState.asStateFlow()

    init {
        loadMockAccounts()
    }

    private fun loadMockAccounts() {
        _uiState.value = _uiState.value.copy(
            accounts = listOf(
                Account(id = 1, nome = "Carteira", saldoInicial = 150.50, saldoAtual = 150.50),
                Account(id = 2, nome = "Banco Inter", saldoInicial = 2500.00, saldoAtual = 2500.00),
                Account(id = 3, nome = "Nubank", saldoInicial = 1200.75, saldoAtual = 1200.75),
                Account(id = 4, nome = "Poupança", saldoInicial = 5000.00, saldoAtual = 5000.00)
            )
        )
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingAccount = null,
            nameInput = "",
            balanceInput = ""
        )
    }

    fun showEditDialog(account: Account) {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingAccount = account,
            nameInput = account.nome,
            balanceInput = account.saldoAtual.toString()
        )
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(isDialogVisible = false)
    }

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(nameInput = newName)
    }

    fun onBalanceChange(newBalance: String) {
        _uiState.value = _uiState.value.copy(balanceInput = newBalance)
    }

    fun saveAccount() {
        // Simular salvamento
        hideDialog()
    }

    fun deleteAccount(account: Account) {
        // Simular exclusão
    }
}
