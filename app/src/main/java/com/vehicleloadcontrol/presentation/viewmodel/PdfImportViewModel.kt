package com.vehicleloadcontrol.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicleloadcontrol.data.repository.VehicleRepository
import com.vehicleloadcontrol.domain.model.ShippingDocument
import com.vehicleloadcontrol.domain.model.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfImportViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _extractedDocument = MutableStateFlow<ShippingDocument?>(null)
    val extractedDocument: StateFlow<ShippingDocument?> = _extractedDocument.asStateFlow()

    private val _extractedVehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val extractedVehicles: StateFlow<List<Vehicle>> = _extractedVehicles.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow<String?>(null)
    val success: StateFlow<String?> = _success.asStateFlow()

    fun importPdf(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                _isProcessing.value = true
                val result = repository.extractAndSavePdfData(context, uri)
                result.onSuccess { (document, vehicles) ->
                    _extractedDocument.value = document
                    _extractedVehicles.value = vehicles
                    _success.value = "PDF importado: ${vehicles.size} vehículos"
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al importar PDF"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccess() {
        _success.value = null
    }
}
