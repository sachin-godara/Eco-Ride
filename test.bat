@echo off
echo ==================================================================
echo   EcoRide Automated Verification Suite
echo   Running Comprehensive Assertion Tests
echo ==================================================================

if not exist bin\com\bicyclerental\main\SystemVerificationTest.class (
    echo [INFO] Compiling latest test classes...
    javac -encoding UTF-8 -cp "lib/*" -d bin src\com\bicyclerental\model\*.java src\com\bicyclerental\dao\*.java src\com\bicyclerental\service\*.java src\com\bicyclerental\util\*.java src\com\bicyclerental\ui\*.java src\com\bicyclerental\main\*.java
)

java -ea -cp "bin;lib/*" com.bicyclerental.main.SystemVerificationTest
if %ERRORLEVEL% equ 0 (
    echo.
    echo ==================================================================
    echo [VERIFIED] All functional assertions and edge cases passed 100%%!
    echo ==================================================================
) else (
    echo.
    echo [FAILURE] System test assertions failed.
)
pause
