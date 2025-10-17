@echo off
echo Limpiando proyecto...
cd /d "C:\Users\gwinazki.ELJUMILLANO\AndroidStudioProjects\CicloCafe"

echo Eliminando carpeta build...
if exist "app\build" rmdir /s /q "app\build"
if exist "build" rmdir /s /q "build"
if exist ".gradle" rmdir /s /q ".gradle"

echo Ejecutando Gradle Clean...
call gradlew clean

echo Limpieza completada. Ahora puedes compilar el proyecto nuevamente.
pause

