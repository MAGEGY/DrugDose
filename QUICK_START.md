# DrugDose - Quick Start Guide

## Opening the Project

1. **Launch Android Studio**
2. **File → Open**
3. **Navigate to**: `c:\Users\mag\AndroidStudioProjects\DrugDose\android`
4. **Click Open**
5. **Wait for Gradle sync** (2-5 minutes on first load)

## Running the App

### On Emulator
1. **Tools → Device Manager**
2. **Create Device** (if needed) - Select API 35
3. **Start the emulator**
4. **Run → Run 'app'** (or press Shift+F10)

### On Physical Device
1. **Enable USB Debugging** on your Android phone
2. **Connect via USB cable**
3. **Run → Run 'app'**

## Building APK

### Debug APK (for testing)
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```
Output: `android/app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for distribution)
```
Build → Build Bundle(s) / APK(s) → Build Bundle(s)
```
Output: `android/app/build/outputs/bundle/release/app-release.aab`

## Project Structure at a Glance

```
android/
├── app/src/main/kotlin/com/mag5112005/drugdose/
│   ├── MainActivity.kt                    ← App entry point
│   ├── data/
│   │   ├── models/PatientData.kt         ← Patient info
│   │   ├── models/DrugData.kt            ← Drug info
│   │   └── DrugDatabase.kt               ← Drug database
│   ├── domain/
│   │   └── DoseCalculator.kt             ← Calculation logic
│   └── ui/
│       ├── navigation/AppNavigation.kt   ← Screen routing
│       ├── screens/                      ← 7 app screens
│       └── theme/                        ← UI styling
└── build.gradle.kts                      ← Dependencies
```

## Key Files to Modify

### Add a New Drug
Edit: `android/app/src/main/kotlin/com/mag5112005/drugdose/data/DrugDatabase.kt`

```kotlin
DrugData(
    id = "9",
    name = "New Drug Name",
    category = "Category",
    routes = listOf("PO", "IV"),
    standardDose = 500.0,
    doseUnit = "mg",
    frequency = "Every 8 hours",
    maxDailyDose = 2000.0,
    // ... other properties
)
```

### Modify Calculation Logic
Edit: `android/app/src/main/kotlin/com/mag5112005/drugdose/domain/DoseCalculator.kt`

### Add a New Screen
1. Create file: `android/app/src/main/kotlin/com/mag5112005/drugdose/ui/screens/NewScreen.kt`
2. Add to navigation: `android/app/src/main/kotlin/com/mag5112005/drugdose/ui/navigation/AppNavigation.kt`

## Testing the App

### Test Case 1: Basic Calculation
- Age: 30, Weight: 70kg, Male
- Drug: Amoxicillin
- Expected: 500mg every 8 hours

### Test Case 2: Pediatric Dosing
- Age: 5, Weight: 20kg
- Drug: Paracetamol
- Expected: Reduced dose (~125mg)

### Test Case 3: Drug Interactions
- Add Warfarin as concomitant medication
- Select Ibuprofen
- Expected: Interaction warning

## Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| Gradle sync fails | File → Sync Now, then File → Invalidate Caches |
| App won't run | Check emulator is started, or device is connected |
| Compilation error | Build → Rebuild Project |
| Emulator slow | Use Android x86 image, allocate more RAM |

## Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Run app | Shift+F10 |
| Debug app | Shift+F9 |
| Build APK | Ctrl+Shift+B |
| Rebuild project | Ctrl+Shift+F9 |
| Format code | Ctrl+Alt+L |
| Find in files | Ctrl+Shift+F |

## Dependencies Overview

- **Jetpack Compose**: Modern UI framework
- **Material 3**: Design system
- **Navigation**: Screen routing
- **Room**: Database (prepared)
- **DataStore**: Preferences storage
- **Coroutines**: Async operations

## Documentation Files

- **README.md**: Feature overview and architecture
- **SETUP.md**: Detailed build and development guide
- **IMPLEMENTATION_SUMMARY.md**: What was built
- **QUICK_START.md**: This file

## Next Steps

1. ✅ Open project in Android Studio
2. ✅ Run on emulator/device
3. ✅ Test all screens
4. ⬜ Add Room Database for persistence
5. ⬜ Implement localization strings
6. ⬜ Add unit tests
7. ⬜ Integrate FDA drug database API

## Support

- **Android Docs**: https://developer.android.com
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Material Design 3**: https://m3.material.io

---

**Ready to build?** Open Android Studio and start coding! 🚀
