# Script para instalar y ejecutar CicloCafe en PowerShell
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Instalando y ejecutando CicloCafe" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/3] Construyendo APK..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: No se pudo construir el APK" -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host ""
Write-Host "[2/3] Instalando en dispositivo..." -ForegroundColor Yellow
& .\gradlew.bat installDebug

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: No se pudo instalar. Asegurate de tener un dispositivo conectado." -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host ""
Write-Host "[3/3] Ejecutando aplicacion..." -ForegroundColor Yellow
adb shell am start -n com.lavazza.ciclocafe/.SplashActivity

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Aplicacion lanzada exitosamente!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Read-Host "Presiona Enter para salir"

