package com.vehicleloadcontrol.data.repository

import com.vehicleloadcontrol.data.local.dao.VehicleDao
import com.vehicleloadcontrol.data.local.dao.ShippingDocumentDao
import com.vehicleloadcontrol.data.local.dao.TrackingInfoDao
import com.vehicleloadcontrol.domain.model.Vehicle
import com.vehicleloadcontrol.domain.model.ShippingDocument
import com.vehicleloadcontrol.domain.model.TrackingInfo
import com.vehicleloadcontrol.data.pdf.PdfExtractorService
import com.vehicleloadcontrol.data.remote.tracking.TrackingService
import com.vehicleloadcontrol.domain.model.CarrierType
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val vehicleDao: VehicleDao,
    private val shippingDocumentDao: ShippingDocumentDao,
    private val trackingInfoDao: TrackingInfoDao,
    private val pdfExtractorService: PdfExtractorService,
    private val trackingService: TrackingService
) {
    // Vehicle operations
    suspend fun addVehicle(vehicle: Vehicle): Long = vehicleDao.insert(vehicle)
    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.update(vehicle)
    suspend fun deleteVehicle(vehicle: Vehicle) = vehicleDao.delete(vehicle)
    suspend fun getVehicleById(id: Long): Vehicle? = vehicleDao.getById(id)
    fun getVehiclesByBlNumber(blNumber: String): Flow<List<Vehicle>> = vehicleDao.getByBlNumber(blNumber)
    fun getVehiclesByShipmentId(shipmentId: Long): Flow<List<Vehicle>> = vehicleDao.getByShipmentId(shipmentId)
    fun getAllVehicles(): Flow<List<Vehicle>> = vehicleDao.getAllVehicles()
    
    // Shipping Document operations
    suspend fun addShippingDocument(document: ShippingDocument): Long = shippingDocumentDao.insert(document)
    suspend fun updateShippingDocument(document: ShippingDocument) = shippingDocumentDao.update(document)
    suspend fun deleteShippingDocument(document: ShippingDocument) = shippingDocumentDao.delete(document)
    suspend fun getShippingDocumentById(id: Long): ShippingDocument? = shippingDocumentDao.getById(id)
    suspend fun getShippingDocumentByBlNumber(blNumber: String): ShippingDocument? = shippingDocumentDao.getByBlNumber(blNumber)
    fun getAllShippingDocuments(): Flow<List<ShippingDocument>> = shippingDocumentDao.getAllDocuments()
    fun getShippingDocumentsByStatus(status: String): Flow<List<ShippingDocument>> = shippingDocumentDao.getDocumentsByStatus(status)
    fun getShippingDocumentsByCarrier(carrier: String): Flow<List<ShippingDocument>> = shippingDocumentDao.getDocumentsByCarrier(carrier)
    
    // Tracking operations
    suspend fun updateTracking(trackingInfo: TrackingInfo) = trackingInfoDao.update(trackingInfo)
    suspend fun getTrackingByBlNumber(blNumber: String): TrackingInfo? = trackingInfoDao.getByBlNumber(blNumber)
    fun observeTrackingByBlNumber(blNumber: String): Flow<TrackingInfo?> = trackingInfoDao.observeByBlNumber(blNumber)
    fun getAllTracking(): Flow<List<TrackingInfo>> = trackingInfoDao.getAllTracking()
    
    // PDF operations
    suspend fun extractAndSavePdfData(context: Context, uri: Uri): Result<Pair<ShippingDocument?, List<Vehicle>>> {
        return try {
            val (document, vehicles) = pdfExtractorService.extractPdfData(context, uri)
            
            document?.let {
                val docId = shippingDocumentDao.insert(it)
                vehicles.forEach { vehicle ->
                    vehicleDao.insert(vehicle.copy(shipmentId = docId))
                }
            }
            
            Result.success(Pair(document, vehicles))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Tracking refresh
    suspend fun refreshTracking(blNumber: String, carrier: CarrierType): Result<TrackingInfo> {
        return try {
            val trackingInfo = trackingService.getTracking(blNumber, carrier)
            if (trackingInfo != null) {
                trackingInfoDao.insert(trackingInfo)
                Result.success(trackingInfo)
            } else {
                Result.failure(Exception("Failed to fetch tracking info"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
