# DrugDose - Pharmaceutical Dose Calculator

A native Android application for accurate pharmaceutical dose calculations based on FDA guidelines and patient-specific factors.

## Features

### Core Functionality
- **Patient Data Collection**: Age, weight, height, gender, renal/hepatic function
- **Drug Selection**: Searchable database of FDA-approved medications
- **Dose Calculation**: Intelligent dosing based on:
  - Age-based adjustments (pediatric, adult, geriatric)
  - Weight-based formulas
  - Renal function adjustments
  - Hepatic function adjustments
  - Pregnancy/lactation status
- **Drug Interactions**: Real-time interaction checking with concomitant medications
- **Precautions & Warnings**: Clinical safety information and contraindications
- **Calculation History**: Save and review past calculations
- **Multi-language Support**: English, Arabic, French, Spanish, German, Chinese
- **Offline-First**: All data stored locally on device

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/mag5112005/drugdose/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   └── models/
│   │   │   │       ├── PatientData.kt
│   │   │   │       └── DrugData.kt
│   │   │   ├── domain/
│   │   │   │   └── DoseCalculator.kt
│   │   │   └── ui/
│   │   │       ├── navigation/
│   │   │       │   └── AppNavigation.kt
│   │   │       ├── screens/
│   │   │       │   ├── HomeScreen.kt
│   │   │       │   ├── PatientInfoScreen.kt
│   │   │       │   ├── MedicationScreen.kt
│   │   │       │   ├── InteractionScreen.kt
│   │   │       │   ├── CalculationScreen.kt
│   │   │       │   ├── HistoryScreen.kt
│   │   │       │   └── SettingsScreen.kt
│   │   │       └── theme/
│   │   │           ├── Theme.kt
│   │   │           ├── Color.kt
│   │   │           └── Type.kt
│   │   ├── AndroidManifest.xml
│   │   └── res/
│   │       └── values/
│   │           ├── strings.xml
│   │           ├── colors.xml
│   │           └── themes.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle.kts
└── README.md
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Compose Navigation
- **Database**: Room (for future implementation)
- **Local Storage**: DataStore Preferences
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)

## Building the Project

### Prerequisites
- Android Studio Koala or later
- JDK 11+
- Android SDK 35

### Steps

1. **Open in Android Studio**
   ```
   File → Open → Select the android/ folder
   ```

2. **Sync Gradle**
   ```
   File → Sync Now
   ```

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Run on Device/Emulator**
   ```
   Run → Run 'app'
   ```

## Screens Overview

### 1. Home Screen
- Main entry point with quick access to:
  - New Calculation
  - View History
  - Settings

### 2. Patient Information Screen
- Collects patient demographics:
  - Age, weight, height
  - Gender
  - Renal function status
  - Hepatic function status
  - Pregnancy/lactation status

### 3. Medication Selection Screen
- Searchable drug database
- Displays drug category and available routes
- Single drug selection

### 4. Drug Interactions Screen
- Add concomitant medications
- Real-time interaction detection
- Warnings for significant interactions

### 5. Calculation Screen
- Displays recommended dose
- Shows dosing details (route, frequency, duration)
- Lists precautions and warnings
- Interaction summary

### 6. History Screen
- View past calculations
- Patient age and drug information
- Calculation dates

### 7. Settings Screen
- Language selection (6 languages)
- Unit preferences (Metric/Imperial)
- Notification settings
- App information

## Dose Calculation Algorithm

The calculator applies adjustments in the following order:

1. **Base Dose** (from drug database)
2. **Age Adjustment**:
   - < 2 years: 25% of adult dose
   - 2-6 years: 50% of adult dose
   - 6-12 years: 75% of adult dose
   - ≥ 65 years: 85% of adult dose

3. **Weight Adjustment**: For patients < 50 kg
4. **Renal Function Adjustment**: If applicable
5. **Hepatic Function Adjustment**: If applicable
6. **Maximum Dose Limit**: Ensures dose doesn't exceed max daily dose

## Future Enhancements

- [ ] Integration with FDA drug database API
- [ ] Room Database for local drug storage
- [ ] Advanced interaction matrix
- [ ] Pharmacogenomics (CYP450 polymorphisms)
- [ ] Pregnancy category warnings
- [ ] Dosing schedule optimization
- [ ] PDF report generation
- [ ] Cloud sync for calculation history
- [ ] Offline drug database updates
- [ ] Barcode scanning for drug lookup

## Safety Disclaimer

This application is for educational and reference purposes only. It should NOT be used as the sole basis for clinical decision-making. Always consult with healthcare professionals and refer to official FDA guidelines and drug prescribing information.

## License

Proprietary - All rights reserved

## Support

For issues or feature requests, contact the development team.
