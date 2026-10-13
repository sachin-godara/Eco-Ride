@echo off
echo ==================================================================
echo   Starting EcoRide Smart Bicycle Mobility System
echo   Enterprise Edition - Aesthetic Light UI Engine
echo ==================================================================

echo [BUILD] Compiling latest application classes...
call compile.bat /nopause

if %ERRORLEVEL% equ 0 (
    java -cp "bin;lib/*" com.bicyclerental.main.MainApplication
) else (
    echo [ERROR] Build failed. Fix compilation errors before running.
    pause
)
