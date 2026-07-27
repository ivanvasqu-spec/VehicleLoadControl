package com.vehicleloadcontrol.presentation.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vehicleloadcontrol.presentation.viewmodel.PdfImportViewModel
import kotlinx.coroutines.launch

@Composable
fun PdfImportScreen(
    viewModel: PdfImportViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isProcessing = viewModel.isProcessing.collectAsState().value
    val extractedDocument = viewModel.extractedDocument.collectAsState().value
    val extractedVehicles = viewModel.extractedVehicles.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val success = viewModel.success.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importPdf(context, uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Importar PDF") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isProcessing) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Procesando PDF...")
                } else if (extractedDocument != null) {
                    Text(
                        "Documento Importado",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("BL: ${extractedDocument.blNumber}")
                        Text("Naviera: ${extractedDocument.carrier}")
                        Text("Buque: ${extractedDocument.shipName}")
                        Text("Vehículos: ${extractedVehicles.size}")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { pdfLauncher.launch("application/pdf") }) {
                        Text("Importar otro PDF")
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Selecciona un PDF de conocimiento de embarque",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { pdfLauncher.launch("application/pdf") }) {
                        Text("Seleccionar PDF")
                    }
                }
            }

            error?.let { errorMessage ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                    viewModel.clearError()
                }
            }

            success?.let { successMessage ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(successMessage)
                    viewModel.clearSuccess()
                }
            }
        }
    }
}
