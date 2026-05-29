package com.casamassa.prospera.presentation.lancamento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.presentation.components.DropdownSelector
import com.casamassa.prospera.presentation.components.CategorySelector
import java.text.SimpleDateFormat
import java.util.*

val Color_Green_Success = Color(0xFF2E7D32)
val Color_Neutral_Transfer = Color(0xFF1976D2) // Blue for transfers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LancamentoForm(
    viewModel: LancamentoViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategorySelector by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.dateMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.onDateChange(it) }
                    showDatePicker = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showCategorySelector) {
        Dialog(
            onDismissRequest = { showCategorySelector = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                CategorySelector(
                    allCategories = uiState.categories,
                    onCategorySelected = { category ->
                        viewModel.onCategoryChange(category.id)
                        showCategorySelector = false
                    },
                    onClose = { showCategorySelector = false }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (viewModel.isEditing()) "Editar Lançamento" else "Novo Lançamento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Seletor de Tipo (Segmented Buttons) - 3 options now
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                selected = uiState.type == TransactionType.DESPESA,
                onClick = { viewModel.onTypeChange(TransactionType.DESPESA) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
            ) {
                Text("Despesa")
            }
            SegmentedButton(
                selected = uiState.type == TransactionType.RECEITA,
                onClick = { viewModel.onTypeChange(TransactionType.RECEITA) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
            ) {
                Text("Receita")
            }
            SegmentedButton(
                selected = uiState.type == TransactionType.TRANSFERENCIA,
                onClick = { viewModel.onTypeChange(TransactionType.TRANSFERENCIA) },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
            ) {
                Text("Transf.")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Valor
        Text(
            text = "Valor",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextField(
            value = uiState.value,
            onValueChange = { viewModel.onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = when (uiState.type) {
                    TransactionType.DESPESA -> MaterialTheme.colorScheme.error
                    TransactionType.RECEITA -> Color_Green_Success
                    TransactionType.TRANSFERENCIA -> Color_Neutral_Transfer
                }
            ),
            placeholder = {
                Text(
                    "R$ 0,00",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.outlineVariantStacking()
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Descrição
        OutlinedTextField(
            value = uiState.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Data
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Data", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = dateFormatter.format(Date(uiState.dateMillis)),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conta de Origem
        val selectedAccountName = uiState.accounts.find { it.id == uiState.selectedAccountId }?.nome ?: "Selecione"
        DropdownSelector(
            label = if (uiState.type == TransactionType.TRANSFERENCIA) "Conta de Origem" else "Conta",
            options = uiState.accounts.map { it.nome to it.id },
            selectedOption = selectedAccountName,
            onOptionSelected = { viewModel.onAccountChange(it) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (uiState.type == TransactionType.TRANSFERENCIA) {
            // Conta de Destino
            val selectedTargetAccountName = uiState.targetAccounts.find { it.id == uiState.selectedTargetAccountId }?.nome ?: "Selecione"
            DropdownSelector(
                label = "Conta de Destino",
                options = uiState.targetAccounts.map { it.nome to it.id },
                selectedOption = selectedTargetAccountName,
                onOptionSelected = { viewModel.onTargetAccountChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // Seletor Visual de Categoria
            val selectedCategory = uiState.categories.find { it.id == uiState.selectedCategoryId }
            val categoryLabel = if (selectedCategory != null) {
                val parent = uiState.categories.find { it.id == selectedCategory.parentCategoryId }
                if (parent != null) "${parent.nome} -> ${selectedCategory.nome}"
                else selectedCategory.nome
            } else "Selecionar Categoria"

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategorySelector = true },
                shape = MaterialTheme.shapes.extraSmall,
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Categoria",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = categoryLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (selectedCategory != null) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botão Salvar
        Button(
            onClick = { viewModel.salvarLancamento() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Salvar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MaterialTheme.outlineVariantStacking() = colorScheme.outlineVariant
