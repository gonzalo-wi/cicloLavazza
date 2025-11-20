@echo off
echo =========================================
echo  Construyendo APK de CicloCafe
echo =========================================
echo.

cd /d "%~dp0"

echo Limpiando proyecto...
call gradlew.bat clean

echo.
echo Construyendo APK de debug...
call gradlew.bat assembleDebug

echo.
echo =========================================
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo APK generada exitosamente!
    echo Ubicacion: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Abriendo carpeta...
    explorer "app\build\outputs\apk\debug"
) else (
    echo ERROR: No se pudo generar la APK
    echo Revisa los errores anteriores
)
echo =========================================
echo.
pause

