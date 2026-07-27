package com.vehicleloadcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vehicleloadcontrol.data.repository.VehicleRepository
import com.vehicleloadcontrol.domain.model.TrackingInfo
import com.vehicleloadcontrol.domain.model.CarrierType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _trackingInfo = MutableStateFlow<TrackingInfo?>(null)
    val trackingInfo: StateFlow<TrackingInfo?> = _trackingInfo.asStateFlow()

    private val _allTracking = MutableStateFlow<List<TrackingInfo>>(emptyList())
    val allTracking: StateFlow<List<TrackingInfo>> = _allTracking.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllTracking()
    }

    fun loadAllTracking() {
        viewModelScope.launch {
            try {
                repository.getAllTracking().collect { tracking ->
                    _allTracking.value = tracking
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun refreshTracking(blNumber: String, carrier: CarrierType) {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                val result = repository.refreshTracking(blNumber, carrier)
                result.onSuccess { tracking ->
                    _trackingInfo.value = tracking
                    loadAllTracking()
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al actualizar tracking"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
