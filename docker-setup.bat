@echo off
REM CECC Care Companion - Docker Setup Script for Windows
REM This script helps set up and manage the Docker development environment

echo 🚀 CECC Care Companion - Docker Development Environment Setup
echo ==========================================================

if "%1"=="build" (
    echo 🔨 Building Docker image...
    docker-compose build
    goto :end
)

if "%1"=="start" (
    echo ▶️  Starting development environment...
    docker-compose up -d
    goto :end
)

if "%1"=="stop" (
    echo ⏹️  Stopping development environment...
    docker-compose down
    goto :end
)

if "%1"=="shell" (
    echo 🐚 Opening shell in container...
    docker-compose exec android-dev bash
    goto :end
)

if "%1"=="build-app" (
    echo 📱 Building Android application...
    docker-compose exec android-dev bash -c "cd /home/developer/project && ./build-in-docker.sh"
    goto :end
)

if "%1"=="logs" (
    echo 📋 Showing container logs...
    docker-compose logs -f android-dev
    goto :end
)

if "%1"=="clean" (
    echo 🧹 Cleaning Docker environment...
    docker-compose down -v
    docker system prune -f
    goto :end
)

echo 📖 Available commands:
echo   build     - Build the Docker image
echo   start     - Start the development environment
echo   stop      - Stop the development environment
echo   shell     - Open a shell in the container
echo   build-app - Build the Android application
echo   logs      - Show container logs
echo   clean     - Clean Docker environment (removes volumes)
echo.
echo 🚀 Quick start:
echo   docker-setup.bat build
echo   docker-setup.bat start
echo   docker-setup.bat shell
echo   docker-setup.bat build-app
echo.
echo 📚 For more information, see DOCKER_README.md

:end
