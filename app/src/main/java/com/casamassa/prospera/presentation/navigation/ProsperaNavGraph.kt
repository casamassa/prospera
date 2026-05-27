package com.casamassa.prospera.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.casamassa.prospera.ProsperaApplication
import com.casamassa.prospera.presentation.ViewModelFactory
import com.casamassa.prospera.presentation.home.HomeScreen
import com.casamassa.prospera.presentation.fluxo.FluxoScreen
import com.casamassa.prospera.presentation.relatorios.RelatoriosScreen
import com.casamassa.prospera.presentation.configuracoes.ConfiguracoesScreen

@Composable
fun ProsperaNavGraph(navController: NavHostController) {
    val context = LocalContext.current.applicationContext as ProsperaApplication
    val factory = ViewModelFactory(context.accountRepository)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel(factory = factory))
        }
        composable(Screen.Fluxo.route) {
            FluxoScreen()
        }
        composable(Screen.Relatorios.route) {
            RelatoriosScreen()
        }
        composable(Screen.Configuracoes.route) {
            ConfiguracoesScreen()
        }
    }
}
