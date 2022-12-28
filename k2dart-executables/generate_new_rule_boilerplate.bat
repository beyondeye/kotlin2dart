rem use this script to generate boilerplate for integrating a new kotlin2dart rule
rem see the main README.md
rem for installing the kotlin cli see installing_kotlinc.md in kotlinc directory
rem see https://kotlinlang.org/docs/command-line.html#run-scripts

@echo off
rem see https://stackoverflow.com/questions/26551/how-can-i-pass-arguments-to-a-batch-file
rem see https://stackoverflow.com/questions/15567809/batch-extract-path-and-filename-from-a-variable
rem see http://w3schools.org.in/setlocal.htm
setlocal

set arg1=%1

FOR %%i IN ("%arg1%") DO (
set filedrive=%%~di
set filepath=%%~pi
set filename=%%~ni
set fileextension=%%~xi
)

@echo on

..\kotlinc\bin\kotlinc -script ..\kscripts\gen_rule_boilerplate.kts -- -d "%arg1%"
