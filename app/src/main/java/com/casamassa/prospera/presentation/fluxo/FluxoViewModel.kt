package com.casamassa.prospera.presentation.fluxo

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

data class FluxoUiState(
    val selectedCalendar: Calendar = Calendar.getInstance(),
    val transactions: List<FinancialTransaction> = emptyList()
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
            FinancialTransaction(1, "Salário Mensal", 5500.00, System.currentTimeMillis(), TransactionType.RECEITA, 1, 1),
            FinancialTransaction(2, "Supermercado", 850.40, System.currentTimeMillis(), TransactionType.DESPESA, 1, 2),
            FinancialTransaction(3, "Aluguel", 1200.00, System.currentTimeMillis(), TransactionType.DESPESA, 1, 3),
            FinancialTransaction(4, "Venda de Notebook", 1500.00, System.currentTimeMillis(), TransactionType.RECEITA, 1, 4),
            FinancialTransaction(5, "Posto de Gasolina", 220.00, System.currentTimeMillis(), TransactionType.DESPESA, 1, 5),
            FinancialTransaction(6, "Assinatura Streaming", 55.90, System.currentTimeMillis(), TransactionType.DESPESA, 1, 6),
            FinancialTransaction(7, "Academia", 110.00, System.currentTimeMillis(), TransactionType.DESPESA, 1, 7)
        )
        _uiState.value = _uiState.value.copy(transactions = mockData)
    }

    fun getFormattedDate(): String {
        val date = _uiState.value.selectedCalendar.time
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("pt-BR"))
        return sdf.format(date).replaceFirstChar { it.uppercase() }
    }
}
