@echo off
echo ========================================
echo    STOCK PREDICTION SYSTEM
echo ========================================
echo.
echo Starting Backend...
start "Backend" cmd /k "mvn spring-boot:run"

echo.
echo Waiting for backend to start...
timeout /t 8 /nobreak > nul

echo Starting Frontend...
cd frontend
start "Frontend" cmd /k "npm start"

echo.
echo Both Backend and Frontend are starting...
echo Backend: http://localhost:8080/api
echo Frontend: http://localhost:3000
echo.
pause

