package com.vehicleloadcontrol.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vehicleloadcontrol.data.local.dao.ShippingDocumentDao
import com.vehicleloadcontrol.data.local.dao.TrackingInfoDao
import com.vehicleloadcontrol.data.local.dao.VehicleDao
import com.vehicleloadcontrol.domain.model.ShippingDocument
import com.vehicleloadcontrol.domain.model.TrackingInfo
import com.vehicleloadcontrol.domain.model.Vehicle

@Database(
    entities = [
        Vehicle::class,
        ShippingDocument::class,
        TrackingInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VehicleLoadControlDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun shippingDocumentDao(): ShippingDocumentDao
    abstract fun trackingInfoDao(): TrackingInfoDao
}
