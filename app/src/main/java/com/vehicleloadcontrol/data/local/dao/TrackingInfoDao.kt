package com.vehicleloadcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vehicleloadcontrol.domain.model.TrackingInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingInfoDao {
    @Insert
    suspend fun insert(trackingInfo: TrackingInfo): Long

    @Update
    suspend fun update(trackingInfo: TrackingInfo)

    @Delete
    suspend fun delete(trackingInfo: TrackingInfo)

    @Query("SELECT * FROM tracking_info WHERE blNumber = :blNumber")
    suspend fun getByBlNumber(blNumber: String): TrackingInfo?

    @Query("SELECT * FROM tracking_info WHERE blNumber = :blNumber")
    fun observeByBlNumber(blNumber: String): Flow<TrackingInfo?>

    @Query("SELECT * FROM tracking_info ORDER BY updatedAt DESC")
    fun getAllTracking(): Flow<List<TrackingInfo>>

    @Query("DELETE FROM tracking_info WHERE blNumber = :blNumber")
    suspend fun deleteByBlNumber(blNumber: String)
}
