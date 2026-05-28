package com.casamassa.prospera.presentation.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Temporary wrapper for UI display of subcategories
data class CategoryUi(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val subcategories: List<Category> = emptyList()
)

data class CategoriasUiState(
    val categories: List<CategoryUi> = emptyList(),
    val isDialogVisible: Boolean = false,
    val editingCategory: Category? = null,
    val nameInput: String = "",
    val selectedType: TransactionType = TransactionType.DESPESA,
    val selectedParentCategoryId: Long? = null,
    val isSaving: Boolean = false
)

class CategoriasViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _dialogState = MutableStateFlow(DialogState())

    val uiState: StateFlow<CategoriasUiState> = combine(
        categoryRepository.getAllCategories(),
        _dialogState
    ) { allCategories, dState ->
        val parents = allCategories.filter { it.parentCategoryId == null }
        val categoryUiList = parents.map { parent ->
            CategoryUi(
                id = parent.id,
                name = parent.nome,
                type = parent.tipo,
                subcategories = allCategories.filter { it.parentCategoryId == parent.id }
            )
        }

        CategoriasUiState(
            categories = categoryUiList,
            isDialogVisible = dState.isVisible,
            editingCategory = dState.editingCategory,
            nameInput = dState.nameInput,
            selectedType = dState.selectedType,
            selectedParentCategoryId = dState.selectedParentCategoryId,
            isSaving = dState.isSaving
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoriasUiState()
    )

    fun showAddDialog() {
        _dialogState.value = DialogState(isVisible = true)
    }

    fun showEditCategoryDialog(category: CategoryUi) {
        _dialogState.value = DialogState(
            isVisible = true,
            editingCategory = Category(category.id, category.name, category.type),
            nameInput = category.name,
            selectedType = category.type,
            selectedParentCategoryId = null
        )
    }

    fun showEditSubcategoryDialog(subcategory: Category, parentName: String) {
        _dialogState.value = DialogState(
            isVisible = true,
            editingCategory = subcategory,
            nameInput = subcategory.nome,
            selectedType = subcategory.tipo,
            selectedParentCategoryId = subcategory.parentCategoryId
        )
    }

    fun hideDialog() {
        _dialogState.value = DialogState(isVisible = false)
    }

    fun onNameChange(newName: String) {
        _dialogState.value = _dialogState.value.copy(nameInput = newName)
    }

    fun onTypeChange(type: TransactionType) {
        _dialogState.value = _dialogState.value.copy(selectedType = type)
    }

    fun onParentCategoryChange(parentId: Long?) {
        _dialogState.value = _dialogState.value.copy(selectedParentCategoryId = parentId)
    }

    fun saveCategory() {
        val name = _dialogState.value.nameInput
        if (name.isBlank()) return

        viewModelScope.launch {
            _dialogState.value = _dialogState.value.copy(isSaving = true)
            val editingCategory = _dialogState.value.editingCategory
            val type = _dialogState.value.selectedType
            val parentId = _dialogState.value.selectedParentCategoryId

            if (editingCategory == null) {
                categoryRepository.insertCategory(
                    Category(nome = name, tipo = type, parentCategoryId = parentId)
                )
            } else {
                categoryRepository.updateCategory(
                    editingCategory.copy(nome = name, tipo = type, parentCategoryId = parentId)
                )
            }
            hideDialog()
        }
    }

    fun deleteCategory(category: CategoryUi) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(Category(category.id, category.name, category.type))
        }
    }

    fun deleteSubcategory(subcategory: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(subcategory)
        }
    }

    private data class DialogState(
        val isVisible: Boolean = false,
        val editingCategory: Category? = null,
        val nameInput: String = "",
        val selectedType: TransactionType = TransactionType.DESPESA,
        val selectedParentCategoryId: Long? = null,
        val isSaving: Boolean = false
    )
}
