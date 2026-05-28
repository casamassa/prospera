package com.casamassa.prospera.presentation.fluxo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.CategoryRepository
import com.casamassa.prospera.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class TransactionUi(
    val transaction: FinancialTransaction,
    val categoryName: String
)

data class FluxoUiState(
    val selectedCalendar: Calendar = Calendar.getInstance(),
    val transactions: List<TransactionUi> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class FluxoViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _selectedCalendar = MutableStateFlow(Calendar.getInstance())
    
    val formattedDate: StateFlow<String> = _selectedCalendar
        .map { calendar ->
            val sdf = SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("pt-BR"))
            sdf.format(calendar.time).replaceFirstChar { it.uppercase() }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val uiState: StateFlow<FluxoUiState> = combine(
        _selectedCalendar.flatMapLatest { calendar ->
            val startOfMonth = calendar.clone() as Calendar
            startOfMonth.set(Calendar.DAY_OF_MONTH, 1)
            startOfMonth.set(Calendar.HOUR_OF_DAY, 0)
            startOfMonth.set(Calendar.MINUTE, 0)
            startOfMonth.set(Calendar.SECOND, 0)
            startOfMonth.set(Calendar.MILLISECOND, 0)
            
            val endOfMonth = calendar.clone() as Calendar
            endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
            endOfMonth.set(Calendar.HOUR_OF_DAY, 23)
            endOfMonth.set(Calendar.MINUTE, 59)
            endOfMonth.set(Calendar.SECOND, 59)
            endOfMonth.set(Calendar.MILLISECOND, 999)
            
            transactionRepository.getTransactionsByDateRange(
                startOfMonth.timeInMillis,
                endOfMonth.timeInMillis
            )
        },
        categoryRepository.getAllCategories(),
        _selectedCalendar
    ) { transactions, categories, calendar ->
        val categoryMap = categories.associateBy { it.id }
        
        val uiTransactions = transactions.map { transaction ->
            val category = categoryMap[transaction.categoriaId]
            val categoryName = if (transaction.tipo == TransactionType.TRANSFERENCIA) {
                "Transferência"
            } else if (category != null) {
                if (category.parentCategoryId != null) {
                    val parent = categoryMap[category.parentCategoryId]
                    if (parent != null) "${parent.nome} -> ${category.nome}"
                    else category.nome
                } else {
                    category.nome
                }
            } else {
                "Sem Categoria"
            }
            
            TransactionUi(transaction, categoryName)
        }.sortedByDescending { it.transaction.dataTimestamp }

        FluxoUiState(
            selectedCalendar = calendar,
            transactions = uiTransactions,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FluxoUiState(isLoading = true)
    )

    fun nextMonth() {
        val nextDate = _selectedCalendar.value.clone() as Calendar
        nextDate.add(Calendar.MONTH, 1)
        _selectedCalendar.value = nextDate
    }

    fun previousMonth() {
        val prevDate = _selectedCalendar.value.clone() as Calendar
        prevDate.add(Calendar.MONTH, -1)
        _selectedCalendar.value = prevDate
    }

    fun deleteTransaction(transaction: FinancialTransaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
            // Note: We might need to revert the balance update if delete is permanent.
            // But usually we should have a RevertTransactionUseCase.
            // For now, let's just delete the record as per Task 018.
        }
    }
}
