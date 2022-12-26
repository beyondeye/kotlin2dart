rem pass to this script a single parameter with the name of the file you want to generate the AST for
rem For example .\test_code\basic\comments.kts will match the file test_code\basic\comments.kts
rem to generate kt2dart.jar run the shadowJarExecutable task and get
rem  copy \ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar to this directory and rename to k2dart.jar
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
java -jar .\k2dart.jar --print-ast  "%arg1%" >"%filedrive%%filepath%%filename%_ast.txt"
