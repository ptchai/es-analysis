@echo off
setlocal

IF "%1" == "-h" GOTO Usage

set config_path=%1

FOR %%A IN ("%config_path%") DO (
    SET "filename=%%~nxA"
)

SET "mount_path=%config_path:\=/%"


IF EXIST "%config_path%" (
	if "%filename%" == "application.yml" (
		docker run --rm -v "%mount_path%":/app/application.yml analysis:0.0.1
		GOTO :EOF
	)
)

docker run --rm analysis:0.0.1
GOTO :EOF

:Usage
echo.
echo Usage: run.cmd [config]
echo.
echo Arguments:
echo   [config]   - Specify your configuration file path (application.yml), or use the default
echo.
echo Options:
echo    -h    - Display this help message.
echo.
