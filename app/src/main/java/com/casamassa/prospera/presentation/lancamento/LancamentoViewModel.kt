package com.casamassa.prospera.presentation.lancamento

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.presentation.fluxo.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class LancamentoUiState(
    val type: TransactionType = TransactionType.DESPESA,
    val value: String = "",
    val description: String = "",
    val dateMillis: Long = Calendar.getInstance().timeInMillis,
    val selectedAccount: String = "Carteira",
    val selectedCategory: String = "Alimentação",
    val isSheetVisible: Boolean = false
)

class LancamentoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LancamentoUiState())
    val uiState: StateFlow<LancamentoUiState> = _uiState.asStateFlow()

    val accounts = listOf("Carteira", "Banco Inter", "Nubank", "Poupança")
    val categories = listOf("Alimentação", "Salário", "Transporte", "Lazer", "Saúde", "Moradia", "Extra")

    fun onTypeChange(type: TransactionType) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onValueChange(value: String) {
        _uiState.value = _uiState.value.copy(value = value)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onDateChange(millis: Long) {
        _uiState.value = _uiState.value.copy(dateMillis = millis)
    }

    fun onAccountChange(account: String) {
        _uiState.value = _uiState.value.copy(selectedAccount = account)
    }

    fun onCategoryChange(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun showSheet() {
        _uiState.value = _uiState.value.copy(isSheetVisible = true)
    }

    fun hideSheet() {
        _uiState.value = _uiState.value.copy(
            isSheetVisible = false,
            value = "",
            description = "",
            type = TransactionType.DESPESA,
            dateMillis = Calendar.getInstance().timeInMillis
        )
    }
}
