package com.casamassa.prospera.presentation.relatorios

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casamassa.prospera.presentation.components.MonthSelector
import java.text.NumberFormat
import java.util.*

@Composable
fun RelatoriosScreen(viewModel: RelatoriosViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Relatórios",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MonthSelector(
            formattedDate = viewModel.getFormattedDate(),
            onPreviousMonth = { viewModel.previousMonth() },
            onNextMonth = { viewModel.nextMonth() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                ReportCard(title = "Despesas por Categoria") {
                    PieChartWithLegend(data = uiState.expenseCategories)
                }
            }

            item {
                ReportCard(title = "Receitas por Categoria") {
                    PieChartWithLegend(data = uiState.revenueCategories)
                }
            }

            item {
                ReportCard(title = "Receitas x Despesas") {
                    ComparisonBarChart(
                        revenue = uiState.totalRevenue,
                        expense = uiState.totalExpense,
                        formatter = currencyFormatter
                    )
                }
            }
        }
    }
}

@Composable
fun ReportCard(title: String, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun PieChartWithLegend(data: List<CategoryData>) {
    if (data.isEmpty()) return
    
    val total = data.sumOf { it.value }.toFloat()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = -90f
            data.forEach { category ->
                val sweepAngle = (category.value.toFloat() / total) * 360f
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
                startAngle += sweepAngle
            }
            
            // Draw a circle in the middle to make it a donut chart (optional, but looks modern)
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 4,
                center = center
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            data.forEach { category ->
                val percentage = (category.value / total) * 100
                LegendItem(category.name, category.color, String.format("%.1f%%", percentage))
            }
        }
    }
}

@Composable
fun LegendItem(name: String, color: Color, percentage: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(12.dp),
            color = color,
            shape = MaterialTheme.shapes.extraSmall
        ) {}
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$name ($percentage)",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ComparisonBarChart(revenue: Double, expense: Double, formatter: NumberFormat) {
    val maxVal = maxOf(revenue, expense).toFloat()
    if (maxVal == 0f) return

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BarItem("Receitas", revenue, Color(0xFF4CAF50), maxVal, formatter)
        BarItem("Despesas", expense, Color(0xFFF44336), maxVal, formatter)
    }
}

@Composable
fun BarItem(label: String, value: Double, color: Color, maxVal: Float, formatter: NumberFormat) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = formatter.format(value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)) {
            val width = (value.toFloat() / maxVal) * size.width
            drawRect(
                color = color.copy(alpha = 0.2f),
                size = size
            )
            drawRect(
                color = color,
                size = Size(width, size.height)
            )
        }
    }
}
