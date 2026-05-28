package com.casamassa.prospera.presentation.contas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ContasUiState(
    val accounts: List<Account> = emptyList(),
    val isDialogVisible: Boolean = false,
    val editingAccount: Account? = null,
    val nameInput: String = "",
    val balanceInput: String = "",
    val errorMessage: String? = null,
    val isSaving: Boolean = false
)

class ContasViewModel(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _dialogState = MutableStateFlow(DialogState())

    val uiState: StateFlow<ContasUiState> = combine(
        accountRepository.getAllActiveAccounts(),
        _dialogState
    ) { accounts, dState ->
        ContasUiState(
            accounts = accounts,
            isDialogVisible = dState.isVisible,
            editingAccount = dState.editingAccount,
            nameInput = dState.nameInput,
            balanceInput = dState.balanceInput,
            errorMessage = dState.errorMessage,
            isSaving = dState.isSaving
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ContasUiState()
    )

    fun showAddDialog() {
        _dialogState.value = DialogState(isVisible = true)
    }

    fun showEditDialog(account: Account) {
        _dialogState.value = DialogState(
            isVisible = true,
            editingAccount = account,
            nameInput = account.nome,
            balanceInput = account.saldoAtual.toString()
        )
    }

    fun hideDialog() {
        _dialogState.value = DialogState(isVisible = false)
    }

    fun onNameChange(newName: String) {
        _dialogState.value = _dialogState.value.copy(nameInput = newName)
    }

    fun onBalanceChange(newBalance: String) {
        if (newBalance.isEmpty() || newBalance.matches(Regex("""^-?\d*[.,]?\d{0,2}$"""))) {
            _dialogState.value = _dialogState.value.copy(balanceInput = newBalance.replace(",", "."))
        }
    }

    fun saveAccount() {
        val name = _dialogState.value.nameInput
        val balance = _dialogState.value.balanceInput.toDoubleOrNull() ?: 0.0

        if (name.isBlank()) {
            _dialogState.value = _dialogState.value.copy(errorMessage = "O nome é obrigatório")
            return
        }

        viewModelScope.launch {
            _dialogState.value = _dialogState.value.copy(isSaving = true)
            val editingAccount = _dialogState.value.editingAccount
            if (editingAccount == null) {
                accountRepository.insertAccount(
                    Account(nome = name, saldoInicial = balance, saldoAtual = balance)
                )
            } else {
                accountRepository.updateAccount(
                    editingAccount.copy(nome = name, saldoAtual = balance)
                )
            }
            hideDialog()
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            val transactions = transactionRepository.getTransactionsByAccount(account.id).first()
            if (transactions.isEmpty()) {
                accountRepository.deleteAccount(account)
            } else {
                accountRepository.updateAccount(account.copy(isActive = false))
            }
        }
    }

    private data class DialogState(
        val isVisible: Boolean = false,
        val editingAccount: Account? = null,
        val nameInput: String = "",
        val balanceInput: String = "",
        val errorMessage: String? = null,
        val isSaving: Boolean = false
    )
}
