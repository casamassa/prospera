package com.casamassa.prospera.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.casamassa.prospera.presentation.home.HomeScreen
import com.casamassa.prospera.presentation.fluxo.FluxoScreen
import com.casamassa.prospera.presentation.relatorios.RelatoriosScreen

@Composable
fun ProsperaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Fluxo.route) {
            FluxoScreen()
        }
        composable(Screen.Relatorios.route) {
            RelatoriosScreen()
        }
    }
}
