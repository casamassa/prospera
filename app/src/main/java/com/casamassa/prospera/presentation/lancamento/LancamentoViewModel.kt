package com.casamassa.prospera.presentation.lancamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.AccountRepository
import com.casamassa.prospera.domain.repository.CategoryRepository
import com.casamassa.prospera.domain.use_case.InsertTransactionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class LancamentoUiState(
    val type: TransactionType = TransactionType.DESPESA,
    val value: String = "",
    val description: String = "",
    val dateMillis: Long = Calendar.getInstance().timeInMillis,
    val selectedAccountId: Long? = null,
    val selectedTargetAccountId: Long? = null,
    val selectedCategoryId: Long? = null,
    val isSheetVisible: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val targetAccounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

class LancamentoViewModel(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val insertTransactionUseCase: InsertTransactionUseCase
) : ViewModel() {
    private val _type = MutableStateFlow(TransactionType.DESPESA)
    private val _value = MutableStateFlow("")
    private val _description = MutableStateFlow("")
    private val _dateMillis = MutableStateFlow(Calendar.getInstance().timeInMillis)
    private val _selectedAccountId = MutableStateFlow<Long?>(null)
    private val _selectedTargetAccountId = MutableStateFlow<Long?>(null)
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    private val _isSheetVisible = MutableStateFlow(false)
    private val _isSaving = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _editingTransaction = MutableStateFlow<FinancialTransaction?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<LancamentoUiState> = combine(
        _type, _value, _description, _dateMillis, 
        _selectedAccountId, _selectedTargetAccountId, _selectedCategoryId, 
        _isSheetVisible, _isSaving, _errorMessage,
        accountRepository.getAllActiveAccounts(),
        _type.flatMapLatest { type ->
            if (type == TransactionType.TRANSFERENCIA) flowOf(emptyList())
            else categoryRepository.getCategoriesByType(type)
        }
    ) { params ->
        val type = params[0] as TransactionType
        val value = params[1] as String
        val description = params[2] as String
        val dateMillis = params[3] as Long
        val selectedAccountId = params[4] as Long?
        val selectedTargetAccountId = params[5] as Long?
        val selectedCategoryId = params[6] as Long?
        val isSheetVisible = params[7] as Boolean
        val isSaving = params[8] as Boolean
        val errorMessage = params[9] as String?
        val accounts = params[10] as List<Account>
        val categories = params[11] as List<Category>

        // Auto-select first account if none selected
        if (selectedAccountId == null && accounts.isNotEmpty()) {
            _selectedAccountId.value = accounts.first().id
        }
        
        // Auto-select first category if none selected and not transfer
        if (type != TransactionType.TRANSFERENCIA && selectedCategoryId == null && categories.isNotEmpty()) {
            _selectedCategoryId.value = categories.first().id
        }

        val targetAccounts = accounts.filter { it.id != selectedAccountId }
        if (type == TransactionType.TRANSFERENCIA && selectedTargetAccountId == null && targetAccounts.isNotEmpty()) {
            _selectedTargetAccountId.value = targetAccounts.first().id
        }

        LancamentoUiState(
            type = type,
            value = value,
            description = description,
            dateMillis = dateMillis,
            selectedAccountId = selectedAccountId,
            selectedTargetAccountId = selectedTargetAccountId,
            selectedCategoryId = selectedCategoryId,
            isSheetVisible = isSheetVisible,
            isSaving = isSaving,
            errorMessage = errorMessage,
            accounts = accounts,
            targetAccounts = targetAccounts,
            categories = categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LancamentoUiState()
    )

    fun onTypeChange(type: TransactionType) {
        _type.value = type
        _selectedCategoryId.value = null
        _selectedTargetAccountId.value = null
    }

    fun onValueChange(value: String) {
        if (value.isEmpty() || value.matches(Regex("""^\d*[.,]?\d{0,2}$"""))) {
            _value.value = value.replace(",", ".")
        }
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onDateChange(millis: Long) {
        _dateMillis.value = millis
    }

    fun onAccountChange(accountId: Long) {
        _selectedAccountId.value = accountId
        if (_selectedTargetAccountId.value == accountId) {
            _selectedTargetAccountId.value = null
        }
    }

    fun onTargetAccountChange(accountId: Long) {
        _selectedTargetAccountId.value = accountId
    }

    fun onCategoryChange(categoryId: Long) {
        _selectedCategoryId.value = categoryId
    }

    fun showEditSheet(transaction: FinancialTransaction) {
        _editingTransaction.value = transaction
        _type.value = transaction.tipo
        _value.value = transaction.valor.toString()
        _description.value = transaction.descricao
        _dateMillis.value = transaction.dataTimestamp
        _selectedAccountId.value = transaction.contaId
        _selectedTargetAccountId.value = transaction.transferTargetAccountId
        _selectedCategoryId.value = transaction.categoriaId
        _isSheetVisible.value = true
        _errorMessage.value = null
    }

    fun showSheet() {
        _isSheetVisible.value = true
        _errorMessage.value = null
    }

    fun hideSheet() {
        _isSheetVisible.value = false
        _value.value = ""
        _description.value = ""
        _type.value = TransactionType.DESPESA
        _dateMillis.value = Calendar.getInstance().timeInMillis
        _selectedAccountId.value = null
        _selectedTargetAccountId.value = null
        _selectedCategoryId.value = null
        _errorMessage.value = null
        _editingTransaction.value = null
    }

    fun salvarLancamento() {
        val valueDouble = _value.value.toDoubleOrNull() ?: 0.0
        if (valueDouble <= 0.0) {
            _errorMessage.value = "Valor deve ser maior que zero"
            return
        }
        if (_description.value.isBlank()) {
            _errorMessage.value = "Descrição é obrigatória"
            return
        }
        val accountId = _selectedAccountId.value ?: return
        
        if (_type.value == TransactionType.TRANSFERENCIA) {
            if (_selectedTargetAccountId.value == null) {
                _errorMessage.value = "Conta de destino é obrigatória"
                return
            }
            if (_selectedTargetAccountId.value == accountId) {
                _errorMessage.value = "Conta de destino deve ser diferente da origem"
                return
            }
        } else {
            if (_selectedCategoryId.value == null) {
                _errorMessage.value = "Categoria é obrigatória"
                return
            }
        }

        viewModelScope.launch {
            _isSaving.value = true
            val transaction = FinancialTransaction(
                descricao = _description.value,
                valor = valueDouble,
                dataTimestamp = _dateMillis.value,
                tipo = _type.value,
                contaId = accountId,
                categoriaId = if (_type.value == TransactionType.TRANSFERENCIA) null else _selectedCategoryId.value,
                transferTargetAccountId = if (_type.value == TransactionType.TRANSFERENCIA) _selectedTargetAccountId.value else null
            )
            
            val result = insertTransactionUseCase(transaction)
            _isSaving.value = false
            
            if (result.isSuccess) {
                hideSheet()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Erro desconhecido"
            }
        }
    }
}
