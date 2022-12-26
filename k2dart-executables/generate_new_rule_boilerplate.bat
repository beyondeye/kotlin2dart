rem use this script to generate boilerplate for integrating a new kotlin2dart rule
rem see the main README.md
rem for installing the kotlin cli see installing_kotlinc.md in kotlinc directory
rem see https://kotlinlang.org/docs/command-line.html#run-scripts
..\kotlinc\bin\kotlinc -script _gen_rule_boilerplate.kts -d test_code\prova.kts
