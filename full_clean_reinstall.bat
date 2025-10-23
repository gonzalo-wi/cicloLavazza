@echo off
echo ========================================
echo LIMPIEZA TOTAL Y REINSTALACION COMPLETA
echo ========================================
echo.

cd /d "C:\Users\gwinazki.ELJUMILLANO\AndroidStudioProjects\CicloCafe"

echo [1/7] Eliminando carpetas build y cache...
if exist "app\build" rmdir /s /q "app\build"
if exist "build" rmdir /s /q "build"
if exist ".gradle" rmdir /s /q ".gradle"

echo.
echo [2/7] Limpiando cache de Gradle...
call gradlew clean

echo.
echo [3/7] Limpiando cache de build...
call gradlew cleanBuildCache

echo.
echo [4/7] Recompilando proyecto completo...
call gradlew assembleDebug --rerun-tasks

if errorlevel 1 (
    echo.
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)

echo.
echo [5/7] Buscando dispositivo conectado...
call gradlew deviceCheck
timeout /t 2 /nobreak >nul

echo.
echo [6/7] DESINSTALANDO app antigua completamente...
adb uninstall com.lavazza.ciclocafe
timeout /t 3 /nobreak >nul

echo.
echo [7/7] Instalando nueva version...
call gradlew installDebug

if errorlevel 1 (
    echo.
    echo ERROR: La instalacion fallo
    pause
    exit /b 1
)

echo.
echo ========================================
echo COMPLETADO EXITOSAMENTE!
echo ========================================
echo.
echo La app se desinstalo completamente y se reinstalo desde cero
echo Abre la app manualmente en tu celular
echo.
pause

