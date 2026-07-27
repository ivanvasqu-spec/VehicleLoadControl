package com.vehicleloadcontrol.di

import android.content.Context
import androidx.room.Room
import com.vehicleloadcontrol.data.local.database.VehicleLoadControlDatabase
import com.vehicleloadcontrol.data.pdf.PdfExtractorService
import com.vehicleloadcontrol.data.pdf.PdfExtractorServiceImpl
import com.vehicleloadcontrol.data.remote.tracking.TrackingService
import com.vehicleloadcontrol.data.remote.tracking.TrackingServiceImpl
import com.vehicleloadcontrol.data.repository.VehicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VehicleLoadControlDatabase {
        return Room.databaseBuilder(
            context,
            VehicleLoadControlDatabase::class.java,
            "vehicle_load_control_db"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideVehicleDao(
        database: VehicleLoadControlDatabase
    ) = database.vehicleDao()
    
    @Provides
    @Singleton
    fun provideShippingDocumentDao(
        database: VehicleLoadControlDatabase
    ) = database.shippingDocumentDao()
    
    @Provides
    @Singleton
    fun provideTrackingInfoDao(
        database: VehicleLoadControlDatabase
    ) = database.trackingInfoDao()
    
    @Provides
    @Singleton
    fun providePdfExtractorService(): PdfExtractorService {
        return PdfExtractorServiceImpl()
    }
    
    @Provides
    @Singleton
    fun provideTrackingService(): TrackingService {
        return TrackingServiceImpl()
    }
    
    @Provides
    @Singleton
    fun provideVehicleRepository(
        vehicleDao: com.vehicleloadcontrol.data.local.dao.VehicleDao,
        shippingDocumentDao: com.vehicleloadcontrol.data.local.dao.ShippingDocumentDao,
        trackingInfoDao: com.vehicleloadcontrol.data.local.dao.TrackingInfoDao,
        pdfExtractorService: PdfExtractorService,
        trackingService: TrackingService
    ): VehicleRepository {
        return VehicleRepository(
            vehicleDao,
            shippingDocumentDao,
            trackingInfoDao,
            pdfExtractorService,
            trackingService
        )
    }
}
