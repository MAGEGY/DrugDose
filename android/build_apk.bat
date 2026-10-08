@echo off
REM Build test APK using gradle
cd /d "%~dp0"

REM Check if gradle is available
where gradle >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Gradle not found in PATH. Please install Gradle or use Android Studio.
    exit /b 1
)

echo Building debug APK...
gradle assembleDebug

if %ERRORLEVEL% equ 0 (
    echo.
    echo Build successful!
    echo APK location: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo Build failed!
    exit /b 1
)
