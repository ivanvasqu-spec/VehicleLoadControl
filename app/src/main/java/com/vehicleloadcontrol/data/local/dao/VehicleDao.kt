package com.vehicleloadcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vehicleloadcontrol.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert
    suspend fun insert(vehicle: Vehicle): Long

    @Update
    suspend fun update(vehicle: Vehicle)

    @Delete
    suspend fun delete(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getById(id: Long): Vehicle?

    @Query("SELECT * FROM vehicles WHERE blNumber = :blNumber")
    fun getByBlNumber(blNumber: String): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE shipmentId = :shipmentId")
    fun getByShipmentId(shipmentId: Long): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles ORDER BY createdAt DESC")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("DELETE FROM vehicles WHERE blNumber = :blNumber")
    suspend fun deleteByBlNumber(blNumber: String)

    @Query("SELECT COUNT(*) FROM vehicles WHERE blNumber = :blNumber")
    suspend fun countByBlNumber(blNumber: String): Int
}
