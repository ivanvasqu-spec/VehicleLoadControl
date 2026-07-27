# Vehicle Load Control

Applicación Android profesional para gestión de carga de vehículos en envíos marítimos. La aplicación permite importar conocimientos de embarque (BL) en PDF, extraer información automática de vehículos, hacer seguimiento en tiempo real y exportar reportes.

## 🚀 Características Principales

### 1. **Importación de PDF**
- Importación de conocimientos de embarque (BL) en formato PDF
- Extracción automática de datos del documento
- Reconocimiento de navieras (CMA CGM, ZIM, Maersk)
- Parseo inteligente de vehículos desde el PDF

### 2. **Gestión de Documentos**
- Lista de conocimientos de embarque registrados
- Seguimiento del estado de cada documento
- Filtrado por naviera y estado
- Información completa del envío (origen, destino, ETA)

### 3. **Gestión de Vehículos**
- Registro detallado de cada vehículo
- Información: VIN, año, color, consignatario
- Búsqueda por número de BL
- Vinculación automática con documentos

### 4. **Seguimiento en Tiempo Real**
- Integración con APIs de navieras:
  - CMA CGM Tracking
  - ZIM Tracking
  - Maersk Tracking
- Ubicación actual, estado y ETA
- Información del buque y viaje
- Actualizaciones periódicas

### 5. **Exportación de Reportes**
- Exportación a Excel
- Reportes por envío
- Datos consolidados de vehículos
- Información de seguimiento

## 🏗️ Arquitectura

La aplicación sigue el patrón **MVVM** con **Clean Architecture**:

```
app/src/main/java/com/vehicleloadcontrol/
├── domain/              # Modelos de dominio
│   └── model/
│       ├── Vehicle.kt
│       ├── ShippingDocument.kt
│       ├── TrackingInfo.kt
│       └── CarrierType.kt
├── data/                # Capa de datos
│   ├── local/          # Base de datos local (Room)
│   │   ├── dao/
│   │   └── database/
│   ├── remote/         # APIs remotas
│   │   ├── api/
│   │   └── tracking/
│   ├── pdf/            # Extracción de PDF
│   └── repository/     # Repository pattern
├── presentation/        # Capa de presentación
│   ├── ui/
│   │   ├── screens/    # Composables principales
│   │   ├── navigation/ # Navegación
│   │   └── theme/      # Tema Material3
│   └── viewmodel/      # ViewModels
├── di/                  # Inyección de dependencias (Hilt)
└── VehicleLoadControlApp.kt
```

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM + Clean Architecture
- **Base de Datos**: Room Database
- **Red**: Retrofit + OkHttp
- **Inyección de Dependencias**: Hilt
- **Procesamiento de PDF**: PDFBox, iText7
- **Exportación de Excel**: Apache POI
- **Coroutines**: Kotlin Coroutines + Flow
- **Navegación**: Jetpack Navigation Compose

## 📦 Dependencias Principales

```gradle
// Core Android
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-runtime-ktx:2.6.2
androidx.activity:activity-compose:1.8.0

// Compose
androidx.compose.ui:ui:1.6.0
androidx.compose.material3:material3:1.1.2

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Hilt
com.google.dagger:hilt-android:2.48

// Retrofit
com.squareup.retrofit2:retrofit:2.10.0
com.squareup.retrofit2:converter-gson:2.10.0

// PDF Processing
com.tom-roush:pdfbox-android:2.0.27.0
com.itextpdf:itext7-core:7.2.5

// Excel Export
org.apache.poi:poi:5.2.5
org.apache.poi:poi-ooxml:5.2.5
```

## 🔧 Configuración e Instalación

### Requisitos
- Android Studio Arctic Fox o superior
- SDK mínimo: Android 7.0 (API 24)
- SDK objetivo: Android 14 (API 34)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/ivanvasqu-spec/VehicleLoadControl.git
cd VehicleLoadControl
```

2. **Abrir en Android Studio**
```bash
open -a "Android Studio" .
```

3. **Sincronizar Gradle**
- Android Studio detectará automáticamente el archivo `build.gradle.kts`
- Haz clic en "Sync Now"

4. **Ejecutar la aplicación**
- Selecciona un dispositivo o emulador
- Haz clic en "Run" o presiona `Shift + F10`

## 📱 Pantallas Principales

### 1. **Pantalla de Documentos** (`ShippingDocumentListScreen`)
- Lista de conocimientos de embarque
- Estado de cada documento
- Botón flotante para agregar nuevos
- Eliminar documentos existentes

### 2. **Pantalla de Vehículos** (`VehicleListScreen`)
- Lista de vehículos registrados
- Búsqueda por BL
- Información detallada por vehículo
- Gestión de vehículos

### 3. **Pantalla de Importación de PDF** (`PdfImportScreen`)
- Selector de archivos PDF
- Procesamiento automático
- Visualización de datos extraídos
- Confirmación de importación

### 4. **Pantalla de Seguimiento** (`TrackingListScreen`)
- Seguimiento de envíos en tiempo real
- Ubicación y estado actual
- ETA y información del buque
- Actualización automática de datos

## 🗄️ Base de Datos

### Entidades Room

**Vehicles**
```kotlin
@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val blNumber: String,
    val vin: String,
    val year: String,
    val color: String,
    // ... más campos
)
```

**ShippingDocuments**
```kotlin
@Entity(tableName = "shipping_documents")
data class ShippingDocument(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val blNumber: String,
    val carrier: String,
    val status: String,
    // ... más campos
)
```

**TrackingInfo**
```kotlin
@Entity(tableName = "tracking_info")
data class TrackingInfo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val blNumber: String,
    val currentLocation: String,
    val eta: String,
    // ... más campos
)
```

## 🔌 Integración con APIs

### Navieras Soportadas

1. **CMA CGM**
   - Endpoint: `https://www.cma-cgm.com/api/tracking`
   - Parámetro: `bl_number`

2. **ZIM**
   - Endpoint: `https://www.zim.com/api/track`
   - Parámetro: `booking_number`

3. **Maersk**
   - Endpoint: `https://api.maersk.com/v1/tracking/documents`
   - Parámetro: `documentValue`

## 📊 ViewModels

### ShippingDocumentViewModel
- Gestión de documentos de envío
- Carga, selección y eliminación
- Estado de carga y errores

### VehicleViewModel
- Gestión de vehículos
- Filtrado por BL
- CRUD de vehículos

### PdfImportViewModel
- Importación de PDFs
- Extracción de datos
- Procesamiento y almacenamiento

### TrackingViewModel
- Seguimiento de envíos
- Actualización de datos
- Gestión de estado

## 🔐 Inyección de Dependencias

La aplicación usa **Hilt** para inyección de dependencias:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): VehicleLoadControlDatabase
    
    @Provides
    @Singleton
    fun provideVehicleRepository(...): VehicleRepository
    // ... más proveedores
}
```

## 📝 Uso de la Aplicación

### Importar un Documento
1. Navega a "Documentos"
2. Presiona el botón "+" o "Importar PDF"
3. Selecciona un archivo PDF del BL
4. La aplicación automáticamente:
   - Extrae información del documento
   - Identifica la naviera
   - Parsea los vehículos
   - Guarda en base de datos

### Ver Vehículos
1. Navega a "Vehículos"
2. Visualiza la lista de todos los vehículos
3. Usa la barra de búsqueda para filtrar por BL
4. Toca un vehículo para ver detalles

### Seguir Envíos
1. Navega a "Seguimiento"
2. Visualiza el estado actual de cada envío
3. Presiona actualizar para obtener datos nuevos
4. Ve ubicación, ETA y información del buque

## 🚧 Características Futuras

- [ ] Exportación a Excel con formatos personalizados
- [ ] Notificaciones de cambios en estado
- [ ] Integración con más navieras
- [ ] Sincronización en la nube
- [ ] Autenticación de usuarios
- [ ] Generación de documentos
- [ ] Modo offline avanzado
- [ ] Análisis y reportes estadísticos

## 🐛 Solución de Problemas

### La aplicación no inicia
- Verifica que tengas Android SDK 24 (API 7.0) mínimo
- Sincroniza Gradle: `gradle clean && gradle sync`
- Limpia el cache: `Build > Clean Project`

### Error al importar PDF
- Verifica que el archivo sea un PDF válido
- Comprueba permisos de lectura en el dispositivo
- El PDF debe contener información estructurada de BL

### Sin datos de seguimiento
- Verifica conexión a internet
- Asegúrate de que el número BL sea válido
- Comprueba si la naviera está soportada

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver `LICENSE` para más detalles.

## 👥 Contribuidores

- **Ivan Vasquez** - Desarrollador Principal

## 📧 Contacto

Para preguntas o sugerencias, contacta a: ivanvasqu@gmail.com

---

**Última actualización**: Julio 2026
