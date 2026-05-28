package com.casamassa.prospera.presentation.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.presentation.components.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onBack: () -> Unit,
    viewModel: CategoriasViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorias") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {
                items(uiState.categories) { category ->
                    CategoryItem(
                        category = category,
                        onEditCategory = { viewModel.showEditCategoryDialog(category) },
                        onDeleteCategory = { viewModel.deleteCategory(category) },
                        onEditSubcategory = { sub -> viewModel.showEditSubcategoryDialog(sub, category.name) },
                        onDeleteSubcategory = { sub -> viewModel.deleteSubcategory(sub) }
                    )
                }
            }

            Button(
                onClick = { viewModel.showAddDialog() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Nova Categoria", fontWeight = FontWeight.Bold)
            }
        }

        if (uiState.isDialogVisible) {
            CategoryDialog(
                uiState = uiState,
                onNameChange = { viewModel.onNameChange(it) },
                onTypeChange = { viewModel.onTypeChange(it) },
                onParentChange = { viewModel.onParentCategoryChange(it) },
                onSave = { viewModel.saveCategory() },
                onCancel = { viewModel.hideDialog() }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryUi,
    onEditCategory: () -> Unit,
    onDeleteCategory: () -> Unit,
    onEditSubcategory: (Category) -> Unit,
    onDeleteSubcategory: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Column {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    Column {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (category.type == TransactionType.RECEITA) "Receita" else "Despesa",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (category.type == TransactionType.RECEITA) Color_Green_Success else MaterialTheme.colorScheme.error
                        )
                    }
                }
                Row {
                    IconButton(onClick = onEditCategory) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDeleteCategory) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        if (expanded) {
            category.subcategories.forEach { sub ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 4.dp, bottom = 4.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${sub.nome}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Row {
                        IconButton(onClick = { onEditSubcategory(sub) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { onDeleteSubcategory(sub) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    uiState: CategoriasUiState,
    onNameChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onParentChange: (Long?) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val parentOptions = remember(uiState.categories) {
        listOf("Nenhuma (Principal)" to null) + uiState.categories.map { it.name to it.id }
    }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(if (uiState.editingCategory == null) "Nova Categoria" else "Editar Categoria") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.nameInput,
                    onValueChange = onNameChange,
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Tipo", style = MaterialTheme.typography.labelMedium)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = uiState.selectedType == TransactionType.DESPESA,
                        onClick = { onTypeChange(TransactionType.DESPESA) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text("Despesa")
                    }
                    SegmentedButton(
                        selected = uiState.selectedType == TransactionType.RECEITA,
                        onClick = { onTypeChange(TransactionType.RECEITA) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text("Receita")
                    }
                }

                val selectedParentName = parentOptions.find { it.second == uiState.selectedParentCategoryId }?.first ?: "Nenhuma (Principal)"
                DropdownSelector(
                    label = "Categoria Pai (Opcional)",
                    options = parentOptions,
                    selectedOption = selectedParentName,
                    onOptionSelected = onParentChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave, enabled = !uiState.isSaving) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Salvar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

// Helper color
val Color_Green_Success = androidx.compose.ui.graphics.Color(0xFF2E7D32)
