rem to generate kt2dart.jar run the shadowJarExecutable task and get
rem  copy \ktlint\build\libs\ktlint-0.48.0-SNAPSHOT-all.jar to this directory and rename to k2dart.jar
java -jar .\k2dart.jar --print-ast  "test_code\miss**.kts" >.\ast.txt
