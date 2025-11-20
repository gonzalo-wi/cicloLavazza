@echo off
echo ========================================
echo  Generando APK de CicloCafe
echo ========================================
echo.

cd /d "%~dp0"

echo Limpiando proyecto anterior...
call gradlew.bat clean
call gradlew.bat assembleDebug --stacktrace
    echo.
    echo *** APK GENERADA EXITOSAMENTE! ***
    echo.
    echo Nombre del archivo: app-debug.apk
    echo Ubicacion completa:
    echo %~dp0app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Tamano del archivo:
    for %%A in ("app\build\outputs\apk\debug\app-debug.apk") do echo %%~zA bytes
    echo.
    echo INSTRUCCIONES PARA INSTALAR:
    echo 1. Copia el archivo app-debug.apk a tu celular
    echo 2. En el celular, abre el archivo APK
    echo 3. Permite la instalacion de fuentes desconocidas si lo pide
    echo 4. Instala la aplicacion
    echo.
    echo *** ERROR: No se pudo generar la APK ***
    echo.
    echo Por favor revisa los errores arriba.
    echo Si el error persiste, intenta:
    echo 1. Cerrar Android Studio
    echo 2. Ejecutar: gradlew.bat clean
    echo 3. Ejecutar: gradlew.bat assembleDebug
echo.
echo.
echo ========================================
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo APK generada exitosamente!
    echo Ubicacion: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Abriendo carpeta de la APK...
    start "" "%~dp0app\build\outputs\apk\debug"
) else (
    echo ERROR: No se pudo generar la APK
)
echo ========================================
pause

