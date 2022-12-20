rem rebuild ktlint jar file
rem .\gradlew.bat shadowJarExecutable

cd .\k2dart-executables\
rem update k2dart.jar to latest built version
copy ..\ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar .\k2dart.jar

rem run k2dart on code in \k2dart-executables\test_code directory
rem the add-opens flag is required as workaround of issue https://github.com/pinterest/ktlint/issues/1391
java --add-opens=java.base/java.lang=ALL-UNNAMED -jar .\k2dart.jar

