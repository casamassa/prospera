package com.casamassa.prospera.presentation.fluxo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.model.TransactionType
import com.casamassa.prospera.presentation.components.MonthSelector
import java.text.NumberFormat
import java.util.*

@Composable
fun FluxoScreen(viewModel: FluxoViewModel = viewModel()) {
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

        Spacer(modifier = Modifier.height(24.dp))

        // Listagem de Lançamentos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(uiState.transactions) { transaction ->
                TransactionItem(transaction, currencyFormatter)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: FinancialTransaction, formatter: NumberFormat) {
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

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
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
                    text = if (isTransfer) "Transferência" else "ID Categoria: ${transaction.categoriaId}",
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
    }
}
