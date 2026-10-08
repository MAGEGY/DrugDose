@echo off
echo ====================================
echo Opening DrugDose in Android Studio
echo ====================================
echo.

REM Try to find Android Studio installation
set "STUDIO_PATH=C:\Program Files\Android\Android Studio\bin\studio64.exe"

if exist "%STUDIO_PATH%" (
    echo Found Android Studio at: %STUDIO_PATH%
    echo Opening project...
    start "" "%STUDIO_PATH%" "%~dp0android"
) else (
    echo Android Studio not found at default location.
    echo Please open Android Studio manually and select:
    echo %~dp0android
    echo.
    pause
)
