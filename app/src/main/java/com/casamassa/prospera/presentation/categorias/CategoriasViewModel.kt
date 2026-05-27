package com.casamassa.prospera.presentation.categorias

import androidx.lifecycle.ViewModel
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Temporary wrapper for UI display of subcategories if they are not in the main domain model as a list
data class CategoryUi(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val subcategories: List<Category> = emptyList()
)

data class CategoriasUiState(
    val categories: List<CategoryUi> = emptyList(),
    val isDialogVisible: Boolean = false,
    val editingCategory: Any? = null, // Can be Category or CategoryUi
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
                CategoryUi(1, "Alimentação", TransactionType.DESPESA, listOf(
                    Category(2, "Supermercado", TransactionType.DESPESA, 1),
                    Category(3, "Restaurante", TransactionType.DESPESA, 1)
                )),
                CategoryUi(4, "Trabalho", TransactionType.RECEITA, listOf(
                    Category(5, "Salário", TransactionType.RECEITA, 4),
                    Category(6, "Bônus", TransactionType.RECEITA, 4)
                )),
                CategoryUi(7, "Moradia", TransactionType.DESPESA, listOf(
                    Category(8, "Aluguel", TransactionType.DESPESA, 7),
                    Category(9, "Energia", TransactionType.DESPESA, 7)
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

    fun showEditCategoryDialog(category: CategoryUi) {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingCategory = category,
            nameInput = category.name,
            selectedType = category.type,
            selectedParentCategory = null
        )
    }

    fun showEditSubcategoryDialog(subcategory: Category, parentName: String) {
        _uiState.value = _uiState.value.copy(
            isDialogVisible = true,
            editingCategory = subcategory,
            nameInput = subcategory.nome,
            selectedType = subcategory.tipo,
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
        hideDialog()
    }

    fun deleteCategory(category: CategoryUi) {}

    fun deleteSubcategory(subcategory: Category) {}
}
