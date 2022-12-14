rem make sure to first build ktlint by running the gradle task shadowJarExecutable ( see the batch file build_k2dart.bat)
rem now copy the ktlint.jar to this directory and rename it k2dart.jar
copy ..\ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar .\k2dart.jar

