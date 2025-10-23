@echo off
echo ========================================
echo COMPILACION CON DETALLES DE ERROR
echo ========================================
echo.

cd /d "C:\Users\gwinazki.ELJUMILLANO\AndroidStudioProjects\CicloCafe"

echo [1/2] Limpiando proyecto...
call gradlew clean

echo.
echo [2/2] Compilando con stacktrace completo...
call gradlew assembleDebug --stacktrace

echo.
echo ========================================
echo Revisa los errores arriba
echo ========================================
pause

