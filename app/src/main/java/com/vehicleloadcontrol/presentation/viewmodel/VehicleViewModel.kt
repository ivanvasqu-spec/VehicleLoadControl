package com.vehicleloadcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicleloadcontrol.data.repository.VehicleRepository
import com.vehicleloadcontrol.domain.model.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    private val _filteredVehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val filteredVehicles: StateFlow<List<Vehicle>> = _filteredVehicles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllVehicles()
    }

    fun loadAllVehicles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getAllVehicles().collect { vehicles ->
                    _vehicles.value = vehicles
                    _filteredVehicles.value = vehicles
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByBlNumber(blNumber: String) {
        viewModelScope.launch {
            try {
                repository.getVehiclesByBlNumber(blNumber).collect { vehicles ->
                    _filteredVehicles.value = vehicles
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            try {
                repository.deleteVehicle(vehicle)
                loadAllVehicles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            try {
                repository.addVehicle(vehicle)
                loadAllVehicles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
