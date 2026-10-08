# DrugDose - Setup & Build Guide

## Quick Start

### 1. Open Project in Android Studio

```
File → Open → Navigate to: c:\Users\mag\AndroidStudioProjects\DrugDose\android
```

### 2. Wait for Gradle Sync
Android Studio will automatically detect and sync the Gradle files. This may take 2-5 minutes on first sync.

### 3. Build the Project

**Option A: Build APK (for testing)**
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

**Option B: Build Release APK (for distribution)**
```
Build → Build Bundle(s) / APK(s) → Build Bundle(s)
```

### 4. Run on Emulator or Device

**On Emulator:**
```
Run → Run 'app'
```

**On Physical Device:**
1. Enable USB Debugging on your Android device
2. Connect via USB
3. Run → Run 'app'

## Project Configuration

### Gradle Files

- **`build.gradle.kts`** (Root): Defines plugin versions
- **`app/build.gradle.kts`**: App-specific dependencies and build configuration
- **`settings.gradle.kts`**: Project structure and repositories

### Key Dependencies

- **Jetpack Compose**: UI framework
- **Material 3**: Design system
- **Navigation Compose**: Screen navigation
- **Room**: Database (prepared for future use)
- **DataStore**: Preferences storage
- **Coroutines**: Async operations

## Development Workflow

### Adding a New Screen

1. Create a new file in `ui/screens/`
2. Define a `@Composable` function
3. Add route to `AppNavigation.kt`
4. Update navigation calls in other screens

### Adding a New Drug

1. Update the drug list in `MedicationScreen.kt`
2. Add drug data to `DrugData` model
3. Update interaction matrix if needed

### Modifying Calculation Logic

1. Edit `domain/DoseCalculator.kt`
2. Update adjustment factors as needed
3. Test with various patient profiles

## Building for Release

### 1. Create Signing Key

```bash
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias drugdose
```

### 2. Configure Signing in `build.gradle.kts`

```kotlin
signingConfigs {
    create("release") {
        storeFile = file("release.keystore")
        storePassword = "your_password"
        keyAlias = "drugdose"
        keyPassword = "your_password"
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
    }
}
```

### 3. Build Release APK

```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

The APK will be located at:
```
app/build/outputs/apk/release/app-release.apk
```

## Troubleshooting

### Gradle Sync Issues

**Problem**: "Failed to sync"
**Solution**: 
1. File → Sync Now
2. If persists: File → Invalidate Caches → Invalidate and Restart

### Compilation Errors

**Problem**: "Unresolved reference"
**Solution**:
1. Check imports are correct
2. Rebuild project: Build → Rebuild Project
3. Check Gradle sync completed successfully

### Emulator Issues

**Problem**: "Emulator not starting"
**Solution**:
1. Tools → Device Manager → Create new device
2. Select API 35 or higher
3. Start the emulator before running app

### Build Errors

**Problem**: "Gradle build failed"
**Solution**:
1. Check Java version: `java -version` (should be 11+)
2. Update Android SDK: Tools → SDK Manager
3. Clean build: Build → Clean Project → Build → Rebuild Project

## Testing

### Unit Tests

Create tests in `app/src/test/kotlin/`

Example:
```kotlin
@Test
fun testDoseCalculation() {
    val calculator = DoseCalculator()
    val patient = PatientData(age = 5, weight = 20.0)
    val drug = DrugData(...)
    val result = calculator.calculateDose(patient, drug, "PO")
    assertEquals(expected, result.dose)
}
```

### Run Tests

```
Run → Run 'Tests'
```

## Performance Optimization

### Proguard/R8 Configuration

Edit `app/proguard-rules.pro` to optimize release builds:

```
-optimizationpasses 5
-dontusemixedcaseclassnames
```

### Compose Optimization

- Use `remember` for state management
- Avoid recomposition with proper key usage
- Use `LazyColumn` for large lists

## Version Management

Update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Increment for each release
    versionName = "1.0.1"  // Semantic versioning
}
```

## Next Steps

1. **Implement Room Database** for drug storage
2. **Add FDA Drug Database** integration
3. **Implement Localization** strings for all languages
4. **Add Unit Tests** for calculation logic
5. **Implement History Persistence** with Room
6. **Add PDF Report Generation** for dose recommendations

## Support

For Android Studio help: https://developer.android.com/studio
For Jetpack Compose: https://developer.android.com/jetpack/compose
