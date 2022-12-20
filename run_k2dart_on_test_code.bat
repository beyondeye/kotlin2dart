rem rebuild ktlint jar file
rem .\gradlew.bat shadowJarExecutable

cd .\k2dart-executables\
rem make sure to first build ktlint by running the gradle task shadowJarExecutable ( see the batch file build_k2dart.bat)
rem now copy the ktlint.jar to this directory and rename it k2dart.jar
copy .\ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar .\k2dart.jar

rem to generate kt2dart.jar run the shadowJarExecutable task and get
rem  copy \ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar to this directory and rename to k2dart.jar
rem the add-opens flag is required as workaround of issue https://github.com/pinterest/ktlint/issues/1391
java --add-opens=java.base/java.lang=ALL-UNNAMED -jar .\k2dart.jar

