package com.vehicleloadcontrol.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vehicleloadcontrol.presentation.ui.screens.ShippingDocumentListScreen
import com.vehicleloadcontrol.presentation.ui.screens.VehicleListScreen
import com.vehicleloadcontrol.presentation.ui.screens.PdfImportScreen
import com.vehicleloadcontrol.presentation.ui.screens.TrackingListScreen

seal class Screen(val route: String) {
    object Documents : Screen("documents")
    object Vehicles : Screen("vehicles")
    object PdfImport : Screen("pdf_import")
    object Tracking : Screen("tracking")
}

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Documents.route
    ) {
        composable(Screen.Documents.route) {
            ShippingDocumentListScreen(
                onAddClick = {
                    navController.navigate(Screen.PdfImport.route)
                }
            )
        }

        composable(Screen.Vehicles.route) {
            VehicleListScreen()
        }

        composable(Screen.PdfImport.route) {
            PdfImportScreen()
        }

        composable(Screen.Tracking.route) {
            TrackingListScreen()
        }
    }
}
