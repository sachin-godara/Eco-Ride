@echo off
echo =========================================================
echo   Compiling EcoRide Bicycle Rental System (Java 17)
echo   Enterprise Build Engine
echo =========================================================

if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib/*" -d bin src\com\bicyclerental\model\*.java src\com\bicyclerental\dao\*.java src\com\bicyclerental\service\*.java src\com\bicyclerental\util\*.java src\com\bicyclerental\ui\*.java src\com\bicyclerental\main\*.java

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation completed with 0 errors!
    echo Classes compiled into \bin directory.
) else (
    echo [ERROR] Compilation failed. Please verify Java 17+ JDK installation.
)
if "%1" neq "/nopause" pause
