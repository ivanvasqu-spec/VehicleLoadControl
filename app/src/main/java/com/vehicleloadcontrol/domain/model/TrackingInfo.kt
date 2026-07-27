package com.vehicleloadcontrol.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracking_info")
data class TrackingInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val blNumber: String,
    val carrier: String,
    val currentLocation: String,
    val status: String,
    val lastUpdate: String,
    val eta: String,
    val nextPort: String = "",
    val vesselName: String = "",
    val rawData: String = "", // Store raw JSON response
    val updatedAt: Long = System.currentTimeMillis()
)
