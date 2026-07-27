package com.vehicleloadcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vehicleloadcontrol.domain.model.ShippingDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface ShippingDocumentDao {
    @Insert
    suspend fun insert(document: ShippingDocument): Long

    @Update
    suspend fun update(document: ShippingDocument)

    @Delete
    suspend fun delete(document: ShippingDocument)

    @Query("SELECT * FROM shipping_documents WHERE id = :id")
    suspend fun getById(id: Long): ShippingDocument?

    @Query("SELECT * FROM shipping_documents WHERE blNumber = :blNumber")
    suspend fun getByBlNumber(blNumber: String): ShippingDocument?

    @Query("SELECT * FROM shipping_documents ORDER BY createdAt DESC")
    fun getAllDocuments(): Flow<List<ShippingDocument>>

    @Query("SELECT * FROM shipping_documents WHERE status = :status ORDER BY createdAt DESC")
    fun getDocumentsByStatus(status: String): Flow<List<ShippingDocument>>

    @Query("SELECT * FROM shipping_documents WHERE carrier = :carrier ORDER BY createdAt DESC")
    fun getDocumentsByCarrier(carrier: String): Flow<List<ShippingDocument>>

    @Query("DELETE FROM shipping_documents WHERE blNumber = :blNumber")
    suspend fun deleteByBlNumber(blNumber: String)

    @Query("SELECT COUNT(*) FROM shipping_documents")
    fun getTotalCount(): Flow<Int>
}
