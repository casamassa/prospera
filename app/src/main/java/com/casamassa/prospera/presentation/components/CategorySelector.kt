package com.casamassa.prospera.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.casamassa.prospera.domain.model.Category
import com.casamassa.prospera.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    allCategories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Despesas, 1: Receitas
    var searchQuery by remember { mutableStateOf("") }

    val filteredCategories = remember(allCategories, selectedTab, searchQuery) {
        val type = if (selectedTab == 0) TransactionType.DESPESA else TransactionType.RECEITA
        allCategories.filter { 
            it.tipo == type && 
            (searchQuery.isEmpty() || it.nome.contains(searchQuery, ignoreCase = true))
        }
    }

    val parentCategories = remember(filteredCategories) {
        filteredCategories.filter { it.parentCategoryId == null }.sortedBy { it.nome.uppercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Selecionar Categoria",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar categoria...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Despesas") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Receitas") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            parentCategories.forEach { parent ->
                item {
                    CategoryListItem(
                        category = parent,
                        isParent = true,
                        onClick = { onCategorySelected(parent) }
                    )
                }
                
                val subcategories = filteredCategories
                    .filter { it.parentCategoryId == parent.id }
                    .sortedBy { it.nome.uppercase() }
                
                items(subcategories) { sub ->
                    CategoryListItem(
                        category = sub,
                        isParent = false,
                        onClick = { onCategorySelected(sub) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryListItem(
    category: Category,
    isParent: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = if (isParent) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = if (isParent) 16.dp else 32.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isParent) category.nome else "-> ${category.nome}",
                style = if (isParent) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) 
                        else MaterialTheme.typography.bodyMedium,
                color = if (isParent) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
