package com.casamassa.prospera.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Fluxo : Screen("fluxo", "Fluxo", Icons.AutoMirrored.Filled.List)
    object Relatorios : Screen("relatorios", "Relatórios", Icons.Default.PieChart)
    object Configuracoes : Screen("configuracoes", "Configurações", Icons.Default.Settings)
}

val bottomNavigationItems = listOf(
    Screen.Home,
    Screen.Fluxo,
    Screen.Relatorios,
    Screen.Configuracoes
)
