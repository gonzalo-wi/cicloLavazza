@echo off
echo ========================================
echo Limpiando proyecto CicloCafe
echo ========================================
echo.

cd /d "%~dp0"

echo [1/5] Deteniendo Gradle daemon...
call gradlew.bat --stop
timeout /t 2 /nobreak >nul

echo.
echo [2/5] Limpiando build...
call gradlew.bat clean
timeout /t 2 /nobreak >nul

echo.
echo [3/5] Eliminando carpetas .gradle y build...
if exist .gradle (
    rmdir /s /q .gradle
    echo    - .gradle eliminado
)
if exist app\build (
    rmdir /s /q app\build
    echo    - app\build eliminado
)
if exist build (
    rmdir /s /q build
    echo    - build eliminado
)

echo.
echo [4/5] Actualizando dependencias...
call gradlew.bat --refresh-dependencies

echo.
echo [5/5] Compilando proyecto...
call gradlew.bat assembleDebug

echo.
echo ========================================
echo Limpieza completada!
echo ========================================
echo.
echo Ahora puedes abrir Android Studio y hacer Sync.
echo.
pause

