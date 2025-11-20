@echo off
echo Generando keystore de debug...
cd /d "%~dp0"

:: Verificar si ya existe el keystore
if exist "app\debug.keystore" (
    echo El keystore de debug ya existe. Eliminando...
    del "app\debug.keystore"
)

:: Generar el keystore de debug
keytool -genkey -v -keystore app\debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"

if exist "app\debug.keystore" (
    echo.
    echo Keystore generado exitosamente!
    echo Ubicacion: app\debug.keystore
) else (
    echo.
    echo ERROR: No se pudo generar el keystore
    echo Asegurate de tener Java JDK instalado
)

pause

