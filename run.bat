@echo off
echo =========================================================
echo   Launching EcoRide Bicycle Rental System (Java Swing)
echo   NSP Minor Project - Mid Term Evaluation
echo   Faculty Guide: Mr. Rohit Kumar (Dept. of AI & DS)
echo =========================================================

if not exist bin\com\bicyclerental\main\MainApplication.class (
    echo [INFO] Binaries not found. Running compile.bat first...
    call compile.bat
)

java -cp "bin;lib/*" com.bicyclerental.main.MainApplication
pause
