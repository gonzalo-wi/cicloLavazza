@echo off
echo ========================================
echo Instalando y ejecutando CicloCafe
echo ========================================
echo.

echo [1/3] Construyendo APK...
call gradlew assembleDebug

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo construir el APK
    pause
    exit /b 1
)

echo.
echo [2/3] Instalando en dispositivo...
call gradlew installDebug

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo instalar. Asegurate de tener un dispositivo conectado.
    pause
    exit /b 1
)

echo.
echo [3/3] Ejecutando aplicacion...
adb shell am start -n com.lavazza.ciclocafe/.SplashActivity

echo.
echo ========================================
echo Aplicacion lanzada exitosamente!
echo ========================================
echo.
pause

