# Android Hundfutter-Scanner (Konzept & Startpunkt)

Dieses Repository enthält einen strukturierten Startpunkt für eine Android‑Studio‑App, die:

- Hundefutter‑Produkte inkl. **Nährwerte**, **Barcode** und **Foto** lokal speichert.
- Barcodes per Kamera scannt und die Daten in der App anzeigt.
- Ausgewählte Produkte als **Essen** speichert und Fortschrittsbalken für empfohlene Mengen führt.

> Hinweis: Das Projekt ist als **Konzept + Referenzcode** gedacht. Du kannst die Snippets direkt in ein neues Android‑Studio‑Projekt übernehmen.

---

## 1) Architektur‑Überblick

**Empfohlene Schichten**

- **UI (Compose oder XML)**: Scan‑Screen, Produkt‑Details, Tages‑Übersicht (Fortschritt)
- **Domain**: Berechnungen für Tagesziele, Nährwert‑Summen
- **Data**: Room‑Datenbank (lokal), Repository, ggf. später Remote‑Sync

---

## 2) Datenmodell (Room)

### Entity: `DogFood`
- `id: Long` (PK)
- `name: String`
- `brand: String`
- `barcode: String` (unique)
- `imageUri: String` (lokaler Pfad oder URL)
- **Nährwerte pro 100g**:
  - `caloriesKcal: Double`
  - `proteinG: Double`
  - `fatG: Double`
  - `fiberG: Double`
  - `ashG: Double`
  - `moistureG: Double`
  - `calciumMg: Double`
  - `phosphorusMg: Double`

### Entity: `MealEntry`
- `id: Long` (PK)
- `dogFoodId: Long` (FK → DogFood)
- `amountG: Double`
- `timestamp: Long` (Millis)

### Entity: `DailyTarget`
- `id: Long` (PK)
- `date: String` (z. B. `2025-02-05`)
- `targetCaloriesKcal: Double`
- `targetProteinG: Double`
- `targetFatG: Double`
- `targetFiberG: Double`

---

## 3) Beispiel‑Kotlin (Room Entities)

```kotlin
@Entity(indices = [Index(value = ["barcode"], unique = true)])
data class DogFood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String,
    val barcode: String,
    val imageUri: String,
    val caloriesKcal: Double,
    val proteinG: Double,
    val fatG: Double,
    val fiberG: Double,
    val ashG: Double,
    val moistureG: Double,
    val calciumMg: Double,
    val phosphorusMg: Double,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DogFood::class,
            parentColumns = ["id"],
            childColumns = ["dogFoodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dogFoodId")]
)
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dogFoodId: Long,
    val amountG: Double,
    val timestamp: Long,
)

@Entity(indices = [Index(value = ["date"], unique = true)])
data class DailyTarget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val targetCaloriesKcal: Double,
    val targetProteinG: Double,
    val targetFatG: Double,
    val targetFiberG: Double,
)
```

---

## 4) Barcode‑Scanning (ML Kit + CameraX)

**Abhängigkeiten (Gradle)**
```gradle
implementation("androidx.camera:camera-camera2:1.3.2")
implementation("androidx.camera:camera-lifecycle:1.3.2")
implementation("androidx.camera:camera-view:1.3.2")
implementation("com.google.mlkit:barcode-scanning:17.2.0")
```

**Flow**
1. Kamera öffnen (CameraX Preview)
2. Frames an ML Kit übergeben
3. Barcode ermitteln → `barcode.rawValue` als String
4. `DogFood` per DAO über Barcode suchen
5. Produkt‑Details anzeigen

---

## 5) Fortschrittsbalken (Beispiel‑Berechnung)

Fortschritt = **Summe der gegessenen Nährwerte** / **Tagesziel**

```kotlin
fun percent(consumed: Double, target: Double): Float {
    if (target <= 0) return 0f
    return ((consumed / target) * 100.0).toFloat().coerceIn(0f, 100f)
}
```

---

## 6) Lokale Test‑DB (Seed‑Daten)

Für schnelle Tests kannst du beim App‑Start eine Handvoll Produkte in die DB schreiben.

```kotlin
val sample = DogFood(
    name = "Lamm & Reis",
    brand = "Beispielmarke",
    barcode = "1234567890123",
    imageUri = "file:///android_asset/sample.png",
    caloriesKcal = 350.0,
    proteinG = 25.0,
    fatG = 12.0,
    fiberG = 3.0,
    ashG = 6.0,
    moistureG = 8.0,
    calciumMg = 1200.0,
    phosphorusMg = 900.0,
)
```

---

## 7) Nächste Schritte

1. Neues Android‑Studio‑Projekt anlegen (Empty Activity)
2. Room + CameraX + ML Kit einbinden
3. Entities/DAO/Database erstellen
4. Scanner‑Screen und Produkt‑Details bauen
5. Meal‑Logging + Fortschritts‑Screen hinzufügen

---

## 8) Konkrete nächste Schritte (Checkliste)

### A) Android‑Studio‑Projekt anlegen
1. **New Project → Empty Activity**
2. Mindest‑SDK wählen (empfohlen: **API 26+**)
3. Optional: **Jetpack Compose** aktivieren (wenn du Compose verwenden möchtest)

### B) Gradle konfigurieren
Füge folgende Abhängigkeiten in dein **app‑Module** (`app/build.gradle`) ein:

```gradle
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

implementation("androidx.camera:camera-camera2:1.3.2")
implementation("androidx.camera:camera-lifecycle:1.3.2")
implementation("androidx.camera:camera-view:1.3.2")
implementation("com.google.mlkit:barcode-scanning:17.2.0")
```

> Falls du Compose nutzt, ergänze die passenden Compose‑Dependencies.

### C) AndroidManifest konfigurieren
```xml
<uses-permission android:name="android.permission.CAMERA" />

<application>
    <!-- Optional: Falls du lokale Bilder aus Assets oder Storage nutzt -->
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
</application>
```

### D) DAO + Database erstellen
```kotlin
@Dao
interface DogFoodDao {
    @Query("SELECT * FROM DogFood WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): DogFood?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(food: DogFood)

    @Query("SELECT * FROM DogFood ORDER BY name ASC")
    fun observeAll(): Flow<List<DogFood>>
}

@Database(
    entities = [DogFood::class, MealEntry::class, DailyTarget::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogFoodDao(): DogFoodDao
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun dailyTargetDao(): DailyTargetDao
}
```

### E) Repository + ViewModel
```kotlin
class DogFoodRepository(private val dao: DogFoodDao) {
    suspend fun getByBarcode(barcode: String) = dao.findByBarcode(barcode)
    suspend fun save(food: DogFood) = dao.upsert(food)
    fun observeAll() = dao.observeAll()
}

class ScanViewModel(private val repository: DogFoodRepository) : ViewModel() {
    private val _result = MutableStateFlow<DogFood?>(null)
    val result: StateFlow<DogFood?> = _result

    fun onBarcodeScanned(value: String) {
        viewModelScope.launch {
            _result.value = repository.getByBarcode(value)
        }
    }
}
```

### F) Scanner‑Screen (Kamera + ML Kit)
**Grobe Schritte**
1. CameraX Preview starten
2. Analyzer einrichten, der Frames an ML Kit gibt
3. `barcode.rawValue` extrahieren
4. `ScanViewModel.onBarcodeScanned(...)` aufrufen
5. Ergebnis anzeigen

### G) Essens‑Logging + Fortschritt
1. `MealEntry` beim Speichern eines Essens schreiben
2. Tages‑Summen berechnen
3. Fortschrittsbalken (pro Nährwert) aktualisieren

---

## 9) Beispiel: Tages‑Summen berechnen
```kotlin
data class NutritionTotals(
    val caloriesKcal: Double,
    val proteinG: Double,
    val fatG: Double,
    val fiberG: Double,
)

fun totalsForDay(entries: List<Pair<MealEntry, DogFood>>): NutritionTotals {
    var calories = 0.0
    var protein = 0.0
    var fat = 0.0
    var fiber = 0.0
    entries.forEach { (entry, food) ->
        val factor = entry.amountG / 100.0
        calories += food.caloriesKcal * factor
        protein += food.proteinG * factor
        fat += food.fatG * factor
        fiber += food.fiberG * factor
    }
    return NutritionTotals(calories, protein, fat, fiber)
}
```

Wenn du möchtest, kann ich im nächsten Schritt ein **vollständiges Android‑Studio‑Projekt** mit den nötigen Dateien erzeugen.
