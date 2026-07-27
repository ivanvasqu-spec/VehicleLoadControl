package com.vehicleloadcontrol.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipping_documents")
data class ShippingDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val blNumber: String,
    val carrier: String, // CMA CGM, ZIM, Maersk
    val shipName: String,
    val shipDate: String,
    val portOfOrigin: String,
    val portOfDestination: String,
    val eta: String = "",
    val vesselCode: String = "",
    val voyage: String = "",
    val pdfPath: String,
    val vehicleCount: Int,
    val status: String, // PENDING, IN_TRANSIT, ARRIVED, DELIVERED
    val trackingNumber: String = "",
    val lastTrackingUpdate: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
