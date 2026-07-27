package com.vehicleloadcontrol.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TrackingApiService {
    // CMA CGM Tracking API
    @GET("api/tracking")
    suspend fun cmaCgmTracking(
        @Query("bl_number") blNumber: String
    ): CmaCgmTrackingResponse

    // ZIM Tracking API
    @GET("track")
    suspend fun zimTracking(
        @Query("booking_number") bookingNumber: String
    ): ZimTrackingResponse

    // Maersk Tracking API
    @GET("v1/tracking/documents")
    suspend fun maerskTracking(
        @Query("documentType") documentType: String = "BL",
        @Query("documentValue") documentValue: String
    ): MaerskTrackingResponse
}

data class CmaCgmTrackingResponse(
    val status: String,
    val currentLocation: String,
    val eta: String,
    val vessel: String,
    val lastUpdate: String
)

data class ZimTrackingResponse(
    val bookingNumber: String,
    val status: String,
    val location: String,
    val eta: String,
    val voyage: String,
    val vessel: String
)

data class MaerskTrackingResponse(
    val data: List<MaerskShipment>
)

data class MaerskShipment(
    val id: String,
    val status: String,
    val location: String,
    val eta: String,
    val vessel: String,
    val voyage: String
)
