package com.vehicleloadcontrol.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val blNumber: String,
    val shipmentId: Long,
    val consignee: String,
    val consigneeAddress: String,
    val vehicleDescription: String,
    val vin: String,
    val year: String,
    val color: String,
    val shipName: String,
    val eta: String,
    val portOfDestination: String,
    val quantity: Int = 1,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
