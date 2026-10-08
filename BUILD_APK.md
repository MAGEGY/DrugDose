# Building DrugDose APK

## Prerequisites

You need **Android Studio** installed with:
- Android SDK 35
- Build Tools 35.0.0+
- JDK 11+

## Method 1: Using Android Studio GUI (Recommended)

### Step 1: Open Project
1. Launch **Android Studio**
2. **File → Open**
3. Navigate to: `c:\Users\mag\AndroidStudioProjects\DrugDose\android`
4. Click **Open**
5. Wait for Gradle sync to complete (2-5 minutes)

### Step 2: Build Debug APK
1. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Wait for build to complete
3. A notification will appear when done

### Step 3: Locate APK
The debug APK will be at:
```
c:\Users\mag\AndroidStudioProjects\DrugDose\android\app\build\outputs\apk\debug\app-debug.apk
```

### Step 4: Install on Device/Emulator
1. Connect Android device via USB (or start emulator)
2. **Run → Run 'app'** (or Shift+F10)
3. Select device and click OK

## Method 2: Using Command Line (Requires Gradle Installation)

### Prerequisites
Install Gradle from: https://gradle.org/install/

### Build Command
```bash
cd c:\Users\mag\AndroidStudioProjects\DrugDose\android
gradle assembleDebug
```

### Output
APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Method 3: Build Release APK

### Step 1: Create Signing Key (First Time Only)
```bash
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias drugdose
```

### Step 2: Configure Signing
Edit `app/build.gradle.kts`:
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

### Step 3: Build Release APK
In Android Studio:
1. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Select **Release** variant
3. Wait for build

Output: `app/build/outputs/apk/release/app-release.apk`

## Troubleshooting

### "Build Failed" Error
1. **Clean project**: Build → Clean Project
2. **Rebuild**: Build → Rebuild Project
3. **Invalidate cache**: File → Invalidate Caches → Invalidate and Restart

### "SDK not found"
1. **Tools → SDK Manager**
2. Install API 35 if not present
3. Install Build Tools 35.0.0+

### "Gradle sync failed"
1. **File → Sync Now**
2. If persists: File → Invalidate Caches → Invalidate and Restart

### "Java not found"
1. Ensure JDK 11+ is installed
2. Set JAVA_HOME environment variable
3. Restart Android Studio

## Testing the APK

### Install on Emulator
1. **Tools → Device Manager**
2. Start an emulator
3. **Run → Run 'app'**

### Install on Physical Device
1. Enable USB Debugging on phone
2. Connect via USB
3. **Run → Run 'app'**
4. Select device from list

### Manual Installation
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## APK Details

- **Debug APK Size**: ~8-12 MB
- **Release APK Size**: ~5-8 MB (with ProGuard)
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Package Name**: com.mag5112005.drugdose

## Build Variants

### Debug Build
- Debuggable
- Larger size
- Faster build time
- Good for development

### Release Build
- Optimized
- Smaller size
- Obfuscated code
- For distribution

## Next Steps After Building

1. **Test on device** - Run through all screens
2. **Test calculations** - Verify dose calculations
3. **Test interactions** - Check drug interaction warnings
4. **Test history** - Verify history saving
5. **Test settings** - Check language/unit changes

## Distribution

To publish to Google Play Store:
1. Build release APK (see Method 3)
2. Sign with your release key
3. Upload to Google Play Console
4. Follow Google Play guidelines

---

**Status**: Ready to build
**Last Updated**: April 7, 2024
