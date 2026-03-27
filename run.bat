@echo off
echo ================================================
echo  Smart Campus Library Management System – Build
echo ================================================

:: Create output directory
if not exist out mkdir out

:: Compile all sources
echo.
echo [1/2] Compiling...
javac -d out -sourcepath src src\lms\gui\MainDashboard.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Check your JDK installation / PATH.
    pause
    exit /b 1
)

echo [1/2] Compilation successful.
echo.

:: Run
echo [2/2] Launching LMS...
java -cp out lms.gui.MainDashboard
