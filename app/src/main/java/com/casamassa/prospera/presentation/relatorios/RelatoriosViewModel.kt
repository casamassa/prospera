package com.casamassa.prospera.presentation.relatorios

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class CategoryData(
    val name: String,
    val value: Double,
    val color: Color
)

data class RelatoriosUiState(
    val selectedDate: LocalDate = LocalDate.now(),
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
        val nextDate = _uiState.value.selectedDate.plusMonths(1)
        _uiState.value = _uiState.value.copy(selectedDate = nextDate)
    }

    fun previousMonth() {
        val prevDate = _uiState.value.selectedDate.minusMonths(1)
        _uiState.value = _uiState.value.copy(selectedDate = prevDate)
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
        val date = _uiState.value.selectedDate
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-BR"))
            .replaceFirstChar { it.uppercase() }
        val year = date.year
        return "$month $year"
    }
}
