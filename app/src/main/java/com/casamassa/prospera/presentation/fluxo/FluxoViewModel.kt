package com.casamassa.prospera.presentation.fluxo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

enum class TransactionType {
    RECEITA, DESPESA
}

data class Transaction(
    val description: String,
    val category: String,
    val value: Double,
    val type: TransactionType
)

data class FluxoUiState(
    val selectedCalendar: Calendar = Calendar.getInstance(),
    val transactions: List<Transaction> = emptyList()
)

class FluxoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FluxoUiState())
    val uiState: StateFlow<FluxoUiState> = _uiState.asStateFlow()

    init {
        loadMockTransactions()
    }

    fun nextMonth() {
        val nextDate = _uiState.value.selectedCalendar.clone() as Calendar
        nextDate.add(Calendar.MONTH, 1)
        _uiState.value = _uiState.value.copy(selectedCalendar = nextDate)
    }

    fun previousMonth() {
        val prevDate = _uiState.value.selectedCalendar.clone() as Calendar
        prevDate.add(Calendar.MONTH, -1)
        _uiState.value = _uiState.value.copy(selectedCalendar = prevDate)
    }

    private fun loadMockTransactions() {
        val mockData = listOf(
            Transaction("Salário Mensal", "Trabalho", 5500.00, TransactionType.RECEITA),
            Transaction("Supermercado", "Alimentação", 850.40, TransactionType.DESPESA),
            Transaction("Aluguel", "Moradia", 1200.00, TransactionType.DESPESA),
            Transaction("Venda de Notebook", "Extra", 1500.00, TransactionType.RECEITA),
            Transaction("Posto de Gasolina", "Transporte", 220.00, TransactionType.DESPESA),
            Transaction("Assinatura Streaming", "Lazer", 55.90, TransactionType.DESPESA),
            Transaction("Academia", "Saúde", 110.00, TransactionType.DESPESA)
        )
        _uiState.value = _uiState.value.copy(transactions = mockData)
    }

    fun getFormattedDate(): String {
        val date = _uiState.value.selectedCalendar.time
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("pt-BR"))
        return sdf.format(date).replaceFirstChar { it.uppercase() }
    }
}
