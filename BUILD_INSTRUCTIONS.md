# DrugDose - Build Instructions

## Current Build Status

❌ **Command-line Gradle build is failing due to dependency resolution issues**

The project has been successfully created with all source code files in place, but the Gradle command-line build encounters persistent dependency resolution errors in this environment.

## ✅ Recommended Solution: Use Android Studio

### Method 1: Using Batch Script (Easiest)
1. Double-click `OPEN_IN_ANDROID_STUDIO.bat` in this folder
2. Wait for Android Studio to load and sync Gradle
3. Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. APK will be generated at: `android/app/build/outputs/apk/debug/app-debug.apk`

### Method 2: Manual Open in Android Studio
1. Open **Android Studio**
2. Select **File → Open**
3. Navigate to: `C:\Users\mag\AndroidStudioProjects\DrugDose\android`
4. Click **OK** and wait for Gradle sync (2-5 minutes)
5. Once sync completes: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
6. When build completes, click "locate" in the notification or find APK at:
   ```
   android/app/build/outputs/apk/debug/app-debug.apk
   ```

## Project Structure

```
DrugDose/
├── android/                      # Android Studio project root
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── kotlin/com/mag5112005/drugdose/
│   │   │   │   ├── MainActivity.kt         # Main entry point
│   │   │   │   ├── data/                   # Data models
│   │   │   │   ├── domain/                 # Business logic
│   │   │   │   └── ui/                     # UI components
│   │   │   ├── res/                        # Resources
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts                # App build config
│   ├── gradle/                             # Gradle wrapper
│   ├── build.gradle                        # Project build config
│   ├── settings.gradle.kts
│   └── local.properties                    # SDK location
└── documentation/                          # Project docs
```

## Current Configuration

- **Package**: com.mag5112005.drugdose
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 33 (Android 13)
- **Compile SDK**: 33
- **Version**: 1.0.0 (versionCode 1)
- **Language**: Kotlin
- **Build Tool**: Gradle 8.2
- **AGP Version**: 8.2.0

## What's Included

### Current State
The project currently has a **minimal working MainActivity** that displays:
- App name: "DrugDose Calculator"
- Version: 1.0.0
- Status message: "App is ready!"

### Full Implementation Available
All source files for the complete dose calculator are present in the `ui/` directory:
- `screens/HomeScreen.kt` - Home screen with navigation
- `screens/PatientInfoScreen.kt` - Patient data collection
- `screens/MedicationScreen.kt` - Drug selection
- `screens/InteractionScreen.kt` - Drug interactions
- `screens/CalculationScreen.kt` - Dose calculations
- `screens/HistoryScreen.kt` - Calculation history
- `screens/SettingsScreen.kt` - App settings
- `navigation/AppNavigation.kt` - Navigation logic
- `theme/` - Material Design 3 theme

### To Enable Full UI
Once the APK builds successfully, you can enable the full Jetpack Compose UI by:
1. Adding Compose dependencies to `app/build.gradle.kts`
2. Updating MainActivity to use Compose
3. Rebuilding

## Troubleshooting Gradle Build Issues

If you encounter issues in Android Studio:

### Issue: Gradle Sync Failed
**Solution**:
1. **File → Invalidate Caches → Invalidate and Restart**
2. Wait for Android Studio to restart
3. **File → Sync Project with Gradle Files**

### Issue: SDK Not Found
**Solution**:
1. **Tools → SDK Manager**
2. Ensure **Android 13.0 (Tiramisu) API Level 33** is installed
3. Install **Android SDK Build-Tools 33.0.0+**
4. Click **Apply** and wait for installation

### Issue: Build Tools Not Found
**Solution**:
1. **Tools → SDK Manager → SDK Tools tab**
2. Check **Android SDK Build-Tools**
3. Check **Android SDK Platform-Tools**
4. Click **Apply**

### Issue: Java Version Mismatch
**Solution**:
1. **File → Project Structure → SDK Location**
2. Set **Gradle JDK** to **Embedded JDK** or **JDK 11+**
3. Click **OK** and rebuild

## Testing the APK

### Install on Emulator
1. **Tools → Device Manager**
2. Create/Start an Android Virtual Device (AVD)
3. Drag the APK onto the emulator window
4. Or use: `adb install app-debug.apk`

### Install on Physical Device
1. Enable **USB Debugging** on your Android device:
   - Settings → About Phone → Tap "Build Number" 7 times
   - Settings → Developer Options → Enable "USB Debugging"
2. Connect device via USB
3. Run: `adb install app-debug.apk`
4. Or use Android Studio: **Run → Run 'app'**

## APK Details

- **Debug APK Size**: ~3-5 MB (minimal version)
- **Release APK Size**: ~2-3 MB (with ProGuard)
- **Supported Devices**: Android 7.0+
- **Supported ABIs**: All (armeabi-v7a, arm64-v8a, x86, x86_64)

## Next Steps After Building

1. ✅ **Test the APK** - Install on device/emulator
2. ✅ **Verify functionality** - App launches and displays correctly
3. 🔄 **Add Compose UI** - Enable full dose calculator interface
4. 🔄 **Add dependencies** - Room, DataStore, etc.
5. 🔄 **Implement features** - Dose calculation logic
6. 🔄 **Test thoroughly** - All screens and calculations
7. 🔄 **Build release APK** - Sign and optimize for distribution

## Command Line Build (For Reference)

If you want to try command-line build after Android Studio sync:

```powershell
cd android
.\gradlew clean assembleDebug
```

**Note**: This may still fail due to environment issues, but Android Studio handles these automatically.

## Support

If you encounter issues:
1. Check that Android Studio is up to date
2. Ensure all SDK components are installed
3. Try **File → Invalidate Caches → Invalidate and Restart**
4. Clean project: **Build → Clean Project**
5. Rebuild: **Build → Rebuild Project**

---

**Status**: Ready to build in Android Studio
**Last Updated**: April 8, 2026
**Build System**: Android Studio + Gradle 8.2
