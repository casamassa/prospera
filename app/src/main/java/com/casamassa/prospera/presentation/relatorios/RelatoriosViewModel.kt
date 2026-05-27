package com.casamassa.prospera.presentation.relatorios

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

data class CategoryData(
    val name: String,
    val value: Double,
    val color: Color
)

data class RelatoriosUiState(
    val selectedCalendar: Calendar = Calendar.getInstance(),
    val expenseCategories: List<CategoryData> = emptyList(),
    val revenueCategories: List<CategoryData> = emptyList(),
    val totalRevenue: Double = 0.0,
    val totalExpense: Double = 0.0
)

class RelatoriosViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RelatoriosUiState())
    val uiState: StateFlow<RelatoriosUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
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

    private fun loadMockData() {
        val expenses = listOf(
            CategoryData("Alimentação", 850.40, Color(0xFFFF5252)),
            CategoryData("Moradia", 1200.00, Color(0xFFFF4081)),
            CategoryData("Transporte", 220.00, Color(0xFF7C4DFF)),
            CategoryData("Lazer", 155.90, Color(0xFF536DFE)),
            CategoryData("Saúde", 110.00, Color(0xFF40C4FF))
        )

        val revenues = listOf(
            CategoryData("Salário", 5500.00, Color(0xFF4CAF50)),
            CategoryData("Extra", 1500.00, Color(0xFF8BC34A)),
            CategoryData("Investimentos", 300.00, Color(0xFFCDDC39))
        )

        val totalExp = expenses.sumOf { it.value }
        val totalRev = revenues.sumOf { it.value }

        _uiState.value = _uiState.value.copy(
            expenseCategories = expenses,
            revenueCategories = revenues,
            totalRevenue = totalRev,
            totalExpense = totalExp
        )
    }

    fun getFormattedDate(): String {
        val date = _uiState.value.selectedCalendar.time
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("pt-BR"))
        return sdf.format(date).replaceFirstChar { it.uppercase() }
    }
}
