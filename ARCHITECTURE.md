# Guía de Arquitectura y Desarrollo

## 📋 Tabla de Contenidos

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Carpetas](#estructura-de-carpetas)
3. [Patrones de Diseño](#patrones-de-diseño)
4. [Flujo de Datos](#flujo-de-datos)
5. [Guías de Código](#guías-de-código)
6. [Testing](#testing)
7. [Buenas Prácticas](#buenas-prácticas)

---

## 🏗️ Arquitectura General

La aplicación sigue el patrón **MVVM (Model-View-ViewModel)** combinado con **Clean Architecture**, dividida en tres capas principales:

### Capas Arquitectónicas

```
┌─────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                      │
│  (UI, ViewModels, Navigation, Screens en Compose)       │
└───────────────────────────────────────────────────────���─┘
                          ↕
┌─────────────────────────────────────────────────────────┐
│                   DOMAIN LAYER                           │
│  (Entities, UseCases, Interfaces, Business Logic)       │
└─────────────────────────────────────────────────────────┘
                          ↕
┌─────────────────────────────────────────────────────────┐
│                    DATA LAYER                            │
│  (Repositories, DAOs, Remote APIs, Local DB)            │
└─────────────────────────────────────────────────────────┘
```

### Ventajas de esta Arquitectura

✅ **Separación de Responsabilidades**: Cada capa tiene responsabilidades claras
✅ **Testabilidad**: Fácil de crear tests unitarios e integración
✅ **Mantenibilidad**: Código organizado y escalable
✅ **Reutilización**: Componentes independientes y reutilizables
✅ **Flexibilidad**: Fácil cambiar implementaciones

---

## 📁 Estructura de Carpetas

```
VehicleLoadControl/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/vehicleloadcontrol/
│       │   │   ├── domain/                    # Capa de Dominio
│       │   │   │   └── model/
│       │   │   │       ├── Vehicle.kt
│       │   │   │       ├── ShippingDocument.kt
│       │   │   │       ├── TrackingInfo.kt
│       │   │   │       └── CarrierType.kt
│       │   │   │
│       │   │   ├── data/                      # Capa de Datos
│       │   │   │   ├── local/
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── VehicleDao.kt
│       │   │   │   │   │   ├── ShippingDocumentDao.kt
│       │   │   │   │   │   └── TrackingInfoDao.kt
│       │   │   │   │   └── database/
│       │   │   │   │       └── VehicleLoadControlDatabase.kt
│       │   │   │   ├── remote/
│       │   │   │   │   ├── api/
│       │   │   │   │   │   └── TrackingApiService.kt
│       │   │   │   │   └── tracking/
│       │   │   │   │       └── TrackingService.kt
│       │   │   │   ├── pdf/
│       │   │   │   │   └── PdfExtractorService.kt
│       │   │   │   ├── repository/
│       │   │   │   │   └── VehicleRepository.kt
│       │   │   │   └── util/
│       │   │   │       ├── DateUtils.kt
│       │   │   │       └── FileUtils.kt
│       │   │   │
│       │   │   ├── presentation/             # Capa de Presentación
│       │   │   │   ├── ui/
│       │   │   │   │   ├── screens/
│       │   │   │   │   │   ├── ShippingDocumentListScreen.kt
│       │   │   │   │   │   ├── VehicleListScreen.kt
│       │   │   │   │   │   ├── PdfImportScreen.kt
│       │   │   │   │   │   └── TrackingListScreen.kt
│       │   │   │   │   ├── navigation/
│       │   │   │   │   │   └── Navigation.kt
│       │   │   │   │   └── theme/
│       │   │   │   │       └── Theme.kt
│       │   │   │   ├── viewmodel/
│       │   │   │   │   ├���─ ShippingDocumentViewModel.kt
│       │   │   │   │   ├── VehicleViewModel.kt
│       │   │   │   │   ├── PdfImportViewModel.kt
│       │   │   │   │   └── TrackingViewModel.kt
│       │   │   │   └── MainActivity.kt
│       │   │   │
│       │   │   ├── di/
│       │   │   │   └── AppModule.kt
│       │   │   │
│       │   │   └── VehicleLoadControlApp.kt
│       │   │
│       │   ├── AndroidManifest.xml
│       │   └── res/
│       │       ├── values/
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── mipmap/
│       │
│       └── test/
│           └── java/com/vehicleloadcontrol/
│               └── (Tests unitarios)
│
├── build.gradle.kts (Gradle del proyecto)
├── settings.gradle.kts
├── gradle.properties
├── proguard-rules.pro
├── README.md
├── ARCHITECTURE.md (Este archivo)
└── .gitignore
```

---

## 🎨 Patrones de Diseño

### 1. Repository Pattern

El Repository abstrae la fuente de datos y proporciona una interfaz uniforme:

```kotlin
class VehicleRepository(
    private val vehicleDao: VehicleDao,
    private val shippingDocumentDao: ShippingDocumentDao,
    private val trackingInfoDao: TrackingInfoDao,
    private val pdfExtractorService: PdfExtractorService,
    private val trackingService: TrackingService
) {
    // Operaciones CRUD
    suspend fun addVehicle(vehicle: Vehicle): Long = vehicleDao.insert(vehicle)
    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.update(vehicle)
    fun getAllVehicles(): Flow<List<Vehicle>> = vehicleDao.getAllVehicles()
    
    // Lógica de negocio
    suspend fun extractAndSavePdfData(context: Context, uri: Uri): Result<Pair<...>> {
        // Implementación compleja
    }
}
```

### 2. MVVM (Model-View-ViewModel)

**ViewModel** maneja la lógica de presentación y expone datos a través de StateFlow:

```kotlin
@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {
    
    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()
    
    fun loadAllVehicles() {
        viewModelScope.launch {
            repository.getAllVehicles().collect { vehicles ->
                _vehicles.value = vehicles
            }
        }
    }
}
```

### 3. Dependency Injection con Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): VehicleLoadControlDatabase {
        return Room.databaseBuilder(
            context,
            VehicleLoadControlDatabase::class.java,
            "vehicle_load_control_db"
        ).build()
    }
}
```

### 4. Repository Pattern para Datos Locales y Remotos

```kotlin
suspend fun refreshTracking(blNumber: String, carrier: CarrierType): Result<TrackingInfo> {
    return try {
        // 1. Obtener datos del servicio remoto
        val trackingInfo = trackingService.getTracking(blNumber, carrier)
        if (trackingInfo != null) {
            // 2. Guardar en base de datos local
            trackingInfoDao.insert(trackingInfo)
            Result.success(trackingInfo)
        } else {
            Result.failure(Exception("Failed to fetch tracking info"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## 🔄 Flujo de Datos

### Flujo Completo de Importación de PDF

```
┌─────────────────────────────────────────────────────────────┐
│  UI (PdfImportScreen)                                        │
│  - Usuario selecciona PDF                                   │
└────────────────────┬────────────────────────────────────────┘
                     │ pdfLauncher.launch()
                     ↓
┌─────────────────────────────────────────────────────────────┐
│  ViewModel (PdfImportViewModel)                             │
│  - importPdf(context, uri)                                  │
│  - Llama repository.extractAndSavePdfData()                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│  Repository (VehicleRepository)                             │
│  - Recibe Context y Uri                                     │
│  - Llama PdfExtractorService                                │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│  PdfExtractorService                                         │
│  - Abre archivo desde Uri                                   │
│  - Extrae texto con PDFBox                                  │
│  - Parsea ShippingDocument y Vehicles                       │
│  - Retorna Pair<ShippingDocument?, List<Vehicle>>           │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│  Repository (VehicleRepository)                             │
│  - Guarda ShippingDocument en DAO                           │
│  - Guarda cada Vehicle con shipmentId                       │
│  - Retorna Result.success()                                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│  ViewModel (PdfImportViewModel)                             │
│  - Actualiza _extractedDocument y _extractedVehicles       │
│  - Emite success message                                    │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────��───────────┐
│  UI (PdfImportScreen)                                        │
│  - Recolecta flows con collectAsState()                     │
│  - Muestra datos extraídos                                  │
│  - Snackbar con mensaje de éxito                            │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de Seguimiento (Tracking)

```
┌──────────────────────────────────────┐
│  UI (TrackingListScreen)              │
│  - Muestra lista de tracking          │
│  - Usuario presiona actualizar        │
└────────────┬─────────────────────────┘
             │ viewModel.refreshTracking(blNumber, carrier)
             ↓
┌──────────────────────────────────────┐
│  ViewModel (TrackingViewModel)        │
│  - Establece _isRefreshing = true    │
│  - Llama repository.refreshTracking() │
└────────────┬─────────────────────────┘
             │
             ↓
┌──────────────────────────────────────┐
│  Repository (VehicleRepository)      │
│  - Llama trackingService.getTracking()│
│  - Guarda en trackingInfoDao          │
└────────────┬─────────────────────────┘
             │
             ↓
┌──────────────────────────────────────┐
│  TrackingService (Remoto)            │
│  - Realiza HTTP call a API de naviera│
│  - Parsea respuesta                  │
│  - Retorna TrackingInfo              │
└────────────┬─────────────────────────┘
             │
             ↓ (Retorna al ViewModel)
┌──────────────────────────────────────┐
│  ViewModel                            │
│  - Emite _trackingInfo                │
│  - _isRefreshing = false              │
└────────────┬─────────────────────────┘
             │
             ↓
┌──────────────────────────────────────┐
│  UI (TrackingListScreen)              │
│  - collectAsState() recibe cambios   │
│  - Actualiza pantalla                │
└──────────────────────────────────────┘
```

---

## 💻 Guías de Código

### Crear un Nuevo ViewModel

```kotlin
@HiltViewModel
class MyFeatureViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            try {
                repository.getData().collect { data ->
                    _uiState.value = UiState.Success(data)
                }
            } catch (e: Exception) {
                _error.value = e.message
                _uiState.value = UiState.Error
            }
        }
    }
}

seal class UiState {
    object Loading : UiState()
    data class Success(val data: List<Any>) : UiState()
    object Error : UiState()
}
```

### Crear una Nueva Pantalla en Compose

```kotlin
@Composable
fun MyFeatureScreen(
    viewModel: MyFeatureViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val error = viewModel.error.collectAsState().value
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Feature") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    LazyColumn {
                        items(uiState.data) { item ->
                            MyItemCard(item)
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorMessage(error)
                }
            }
        }
    }
}
```

### Agregar Nuevas Operaciones a DAOs

```kotlin
@Dao
interface MyEntityDao {
    @Insert
    suspend fun insert(entity: MyEntity): Long
    
    @Update
    suspend fun update(entity: MyEntity)
    
    @Delete
    suspend fun delete(entity: MyEntity)
    
    @Query("SELECT * FROM my_entities WHERE id = :id")
    suspend fun getById(id: Long): MyEntity?
    
    @Query("SELECT * FROM my_entities ORDER BY createdAt DESC")
    fun getAll(): Flow<List<MyEntity>>
}
```

---

## 🧪 Testing

### Test Unitario de ViewModel

```kotlin
class VehicleViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: VehicleViewModel
    private val mockRepository = mockk<VehicleRepository>()
    
    @Before
    fun setUp() {
        viewModel = VehicleViewModel(mockRepository)
    }
    
    @Test
    fun `loadAllVehicles updates vehicles state`() = runTest {
        // Arrange
        val mockVehicles = listOf(
            Vehicle(id = 1, blNumber = "BL123", vin = "VIN123", ...),
            Vehicle(id = 2, blNumber = "BL124", vin = "VIN124", ...)
        )
        coEvery { mockRepository.getAllVehicles() } returns flowOf(mockVehicles)
        
        // Act
        viewModel.loadAllVehicles()
        
        // Assert
        assertEquals(mockVehicles, viewModel.vehicles.value)
    }
}
```

### Test de Integración DAO

```kotlin
@RunWith(AndroidRunner::class)
class VehicleDaoTest {
    
    private lateinit var database: VehicleLoadControlDatabase
    private lateinit var vehicleDao: VehicleDao
    
    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VehicleLoadControlDatabase::class.java
        ).build()
        vehicleDao = database.vehicleDao()
    }
    
    @Test
    fun insertAndRetrieveVehicle() = runTest {
        val vehicle = Vehicle(blNumber = "BL123", vin = "VIN123", ...)
        vehicleDao.insert(vehicle)
        
        val retrieved = vehicleDao.getById(1)
        
        assertEquals(vehicle.blNumber, retrieved?.blNumber)
    }
    
    @After
    fun tearDown() {
        database.close()
    }
}
```

---

## ✅ Buenas Prácticas

### 1. **Manejo de Estados**

```kotlin
// ✅ BUENO: Usar sealed class para estados
seal class UiState {
    object Loading : UiState()
    data class Success(val data: List<Item>) : UiState()
    data class Error(val message: String) : UiState()
}

// ❌ MALO: Múltiples flags booleanos
var isLoading = false
var hasError = false
var errorMessage = ""
```

### 2. **Manejo de Excepciones**

```kotlin
// ✅ BUENO: Usar Result wrapper
suspend fun getData(): Result<Data> {
    return try {
        val data = repository.fetchData()
        Result.success(data)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// ❌ MALO: Lanzar excepciones sin manejo
suspend fun getData(): Data {
    return repository.fetchData() // Puede fallar
}
```

### 3. **Naming Conventions**

```kotlin
// ✅ BUENO: Nombres descriptivos
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

fun loadVehicles() { }
fun deleteVehicleById(id: Long) { }

// ❌ MALO: Nombres genéricos
private val _state = MutableStateFlow(...)
fun load() { }
fun delete(id: Long) { }
```

### 4. **Composables Pequeños y Reutilizables**

```kotlin
// ✅ BUENO: Componentes pequeños
@Composable
fun VehicleCard(vehicle: Vehicle, onDelete: () -> Unit) {
    Card { ... }
}

// ❌ MALO: Composable gigante
@Composable
fun VehicleListScreen() {
    // 500+ líneas de código
}
```

### 5. **Coroutines y Lifecycle Awareness**

```kotlin
// ✅ BUENO: Usar viewModelScope
fun loadData() {
    viewModelScope.launch {
        repository.getData().collect { data ->
            _uiState.value = UiState.Success(data)
        }
    }
}

// ❌ MALO: GlobalScope
fun loadData() {
    GlobalScope.launch {
        // Memory leak potencial
    }
}
```

### 6. **Comentarios y Documentación**

```kotlin
// ✅ BUENO: Documentar comportamiento complejo
/**
 * Extrae datos del PDF y los guarda en base de datos.
 * 
 * @param context Context de la aplicación
 * @param uri URI del archivo PDF
 * @return Result con ShippingDocument y lista de Vehicles
 */
suspend fun extractAndSavePdfData(
    context: Context, 
    uri: Uri
): Result<Pair<ShippingDocument?, List<Vehicle>>>

// ❌ MALO: Comentarios obvios
val vehicles = listOf<Vehicle>() // Lista de vehículos
```

---

## 📚 Recursos Útiles

- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)

---

**Última actualización**: Julio 2026
