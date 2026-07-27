package com.vehicleloadcontrol.presentation.viewmodel

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
class ShippingDocumentViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<List<ShippingDocument>>(emptyList())
    val documents: StateFlow<List<ShippingDocument>> = _documents.asStateFlow()

    private val _selectedDocument = MutableStateFlow<ShippingDocument?>(null)
    val selectedDocument: StateFlow<ShippingDocument?> = _selectedDocument.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllDocuments()
    }

    fun loadAllDocuments() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getAllShippingDocuments().collect { docs ->
                    _documents.value = docs
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectDocument(document: ShippingDocument) {
        _selectedDocument.value = document
    }

    fun deleteDocument(document: ShippingDocument) {
        viewModelScope.launch {
            try {
                repository.deleteShippingDocument(document)
                loadAllDocuments()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
