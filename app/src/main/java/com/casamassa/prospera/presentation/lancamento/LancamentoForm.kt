package com.casamassa.prospera.presentation.lancamento

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
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.presentation.components.DropdownSelector
import java.text.SimpleDateFormat
import java.util.*

val Color_Green_Success = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LancamentoForm(
    viewModel: LancamentoViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")) }
    
    var showDatePicker by remember { mutableStateOf(false) }

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
                text = "Novo Lançamento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Seletor de Tipo (Segmented Buttons)
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                selected = uiState.type == TransactionType.DESPESA,
                onClick = { viewModel.onTypeChange(TransactionType.DESPESA) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Text("Despesa")
            }
            SegmentedButton(
                selected = uiState.type == TransactionType.RECEITA,
                onClick = { viewModel.onTypeChange(TransactionType.RECEITA) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Text("Receita")
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
                color = if (uiState.type == TransactionType.DESPESA) MaterialTheme.colorScheme.error else Color_Green_Success
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
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
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

        // Dropdowns (Conta e Categoria)
        val selectedAccountName = uiState.accounts.find { it.id == uiState.selectedAccountId }?.nome ?: "Selecione"
        DropdownSelector(
            label = "Conta",
            options = uiState.accounts.map { it.nome to it.id },
            selectedOption = selectedAccountName,
            onOptionSelected = { viewModel.onAccountChange(it) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val selectedCategoryName = uiState.categories.find { it.id == uiState.selectedCategoryId }?.nome ?: "Selecione"
        DropdownSelector(
            label = "Categoria",
            options = uiState.categories.map { it.nome to it.id },
            selectedOption = selectedCategoryName,
            onOptionSelected = { viewModel.onCategoryChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

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
