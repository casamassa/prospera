package com.casamassa.prospera.presentation.categorias

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.presentation.fluxo.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Subcategory(
    val name: String,
    val type: TransactionType
)

data class Category(
    val name: String,
    val type: TransactionType,
    val subcategories: List<Subcategory> = emptyList()
)

data class CategoriasUiState(
    val categories: List<Category> = emptyList(),
    val isDialogVisible: Boolean = false,
    val editingCategory: Any? = null, // Can be Category or Subcategory
    val nameInput: String = "",
    val selectedType: TransactionType = TransactionType.DESPESA,
    val selectedParentCategory: String? = null
)

class CategoriasViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriasUiState())
    val uiState: StateFlow<CategoriasUiState> = _uiState.asStateFlow()

    init {
        loadMockCategories()
    }

    private fun loadMockCategories() {
        _uiState.value = _uiState.value.copy(
            categories = listOf(
                Category("Alimentação", TransactionType.DESPESA, listOf(
                    Subcategory("Supermercado", TransactionType.DESPESA),
                    Subcategory("Restaurante", TransactionType.DESPESA)
                )),
                Category("Trabalho", TransactionType.RECEITA, listOf(
                    Subcategory("Salário", TransactionType.RECEITA),
                    Subcategory("Bônus", TransactionType.RECEITA)
                )),
                Category("Moradia", TransactionType.DESPESA, listOf(
                    Subcategory("Aluguel", TransactionType.DESPESA),
                    Subcategory("Energia", TransactionType.DESPESA)
                ))
            )
        )
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingCategory = null,
            nameInput = "",
            selectedType = TransactionType.DESPESA,
            selectedParentCategory = null
        )
    }

    fun showEditCategoryDialog(category: Category) {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingCategory = category,
            nameInput = category.name,
            selectedType = category.type,
            selectedParentCategory = null
        )
    }

    fun showEditSubcategoryDialog(subcategory: Subcategory, parentName: String) {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingCategory = subcategory,
            nameInput = subcategory.name,
            selectedType = subcategory.type,
            selectedParentCategory = parentName
        )
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(isDialogVisible = false)
    }

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(nameInput = newName)
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun onParentCategoryChange(parentName: String?) {
        _uiState.value = _uiState.value.copy(selectedParentCategory = parentName)
    }

    fun saveCategory() {
        // Simular salvamento
        hideDialog()
    }

    fun deleteCategory(category: Category) {
        // Simular exclusão
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        // Simular exclusão
    }
}
