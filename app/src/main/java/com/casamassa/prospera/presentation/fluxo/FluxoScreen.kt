package com.casamassa.prospera.presentation.fluxo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casamassa.prospera.ProsperaApplication
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.domain.use_case.InsertTransactionUseCase
import com.casamassa.prospera.presentation.ViewModelFactory
import com.casamassa.prospera.presentation.components.MonthSelector
import com.casamassa.prospera.presentation.lancamento.LancamentoViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun FluxoScreen(
    viewModel: FluxoViewModel,
    lancamentoViewModel: LancamentoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val formattedDate by viewModel.formattedDate.collectAsState()
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Fluxo de Caixa",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Seletor de Mês
        MonthSelector(
            formattedDate = formattedDate,
            onPreviousMonth = { viewModel.previousMonth() },
            onNextMonth = { viewModel.nextMonth() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Resumo do Mês
        MonthlySummary(uiState, currencyFormatter)

        Spacer(modifier = Modifier.height(16.dp))

        // Listagem de Lançamentos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(uiState.transactions) { transactionUi ->
                TransactionItem(
                    transactionUi = transactionUi, 
                    formatter = currencyFormatter,
                    onEdit = { lancamentoViewModel.showEditSheet(it) },
                    onDelete = { viewModel.deleteTransaction(it) }
                )
            }
        }
    }
}

@Composable
fun MonthlySummary(uiState: FluxoUiState, formatter: NumberFormat) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Receitas", uiState.totalRevenue, Color(0xFF2E7D32), formatter)
                SummaryItem("Despesas", uiState.totalExpense, MaterialTheme.colorScheme.error, formatter)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Saldo do Período", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = formatter.format(uiState.totalBalance),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (uiState.totalBalance >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: Double, color: Color, formatter: NumberFormat) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = formatter.format(value),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun TransactionItem(
    transactionUi: TransactionUi, 
    formatter: NumberFormat,
    onEdit: (FinancialTransaction) -> Unit,
    onDelete: (FinancialTransaction) -> Unit
) {
    val transaction = transactionUi.transaction
    val isTransfer = transaction.tipo == TransactionType.TRANSFERENCIA
    
    val valueColor = when (transaction.tipo) {
        TransactionType.RECEITA -> Color(0xFF2E7D32)
        TransactionType.DESPESA -> MaterialTheme.colorScheme.error
        TransactionType.TRANSFERENCIA -> Color(0xFF1976D2)
    }
    
    val prefix = when (transaction.tipo) {
        TransactionType.RECEITA -> "+"
        TransactionType.DESPESA -> "-"
        TransactionType.TRANSFERENCIA -> ""
    }

    var showMenu by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showMenu = true },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isTransfer) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Transferência",
                        tint = valueColor,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.descricao,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = transactionUi.categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "$prefix ${formatter.format(transaction.valor).replace("R$", "").trim()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        onEdit(transaction)
                        showMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text("Excluir") },
                    onClick = {
                        onDelete(transaction)
                        showMenu = false
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                )
            }
        }
    }
}
