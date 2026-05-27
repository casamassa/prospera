package com.casamassa.prospera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.casamassa.prospera.presentation.lancamento.LancamentoForm
import com.casamassa.prospera.presentation.lancamento.LancamentoViewModel
import com.casamassa.prospera.presentation.navigation.ProsperaNavGraph
import com.casamassa.prospera.presentation.navigation.Screen
import com.casamassa.prospera.presentation.navigation.bottomNavigationItems
import com.casamassa.prospera.presentation.theme.ProsperaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProsperaTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    lancamentoViewModel: LancamentoViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by lancamentoViewModel.uiState.collectAsState()
    
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Force full screen/expanded mode
    )

    // Controlar visibilidade do FAB (Home e Fluxo apenas)
    val showFab = currentDestination?.hierarchy?.any { 
        it.route == Screen.Home.route || it.route == Screen.Fluxo.route 
    } == true

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { lancamentoViewModel.showSheet() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Novo Lançamento")
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProsperaNavGraph(navController = navController)
        }

        // Modal Bottom Sheet para Lançamento
        if (uiState.isSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { lancamentoViewModel.hideSheet() },
                sheetState = sheetState,
                dragHandle = null, // Custom header in LancamentoForm
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                LancamentoForm(
                    viewModel = lancamentoViewModel,
                    onClose = { lancamentoViewModel.hideSheet() }
                )
            }
        }
    }
}
