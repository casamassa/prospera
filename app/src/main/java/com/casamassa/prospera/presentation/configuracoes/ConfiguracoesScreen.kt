package com.casamassa.prospera.presentation.configuracoes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.casamassa.prospera.presentation.contas.ContasScreen
import com.casamassa.prospera.presentation.categorias.CategoriasScreen

@Composable
fun ConfiguracoesScreen() {
    var currentScreen by remember { mutableStateOf("config") }

    when (currentScreen) {
        "contas" -> ContasScreen(onBack = { currentScreen = "config" })
        "categorias" -> CategoriasScreen(onBack = { currentScreen = "config" })
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Configurações",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                ConfigOption(
                    title = "Gerenciar Contas",
                    icon = Icons.Default.AccountBalanceWallet,
                    onClick = { currentScreen = "contas" }
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                ConfigOption(
                    title = "Gerenciar Categorias",
                    icon = Icons.Default.AccountBalanceWallet, // Or a more suitable icon if available
                    onClick = { currentScreen = "categorias" }
                )
            }
        }
    }
}

@Composable
fun ConfigOption(title: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
