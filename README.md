## Kotlin2Dart
This is a Kotlin to Dart transpiler, based  on the [ktlint engine](https://github.com/pinterest/ktlint).

It was not designed to be a complete transpiler, but instead to facilitate manual porting of Kotlin code to Dart.

The output Dart code is most cases will not be actual valid Dart code. The purpose is instead to make it easier and faster
for a programmer to complete the job of porting the code.

For a guide on how different language construct compares in the two languages, see [kotlin is dart](https://beyondeye.github.io/kotlin_is_dart/)

For porting code that relies on kotlin standard libraries and collections, we rely on
the [kt_dart package](https://pub.dev/packages/kt_dart), in other words, ``kt.dart`` will be a required dependency of
the produced code.

## Building k2dart
First clone this repository.
Then, in order to build the ``ktlint.jar`` file, you need to run the ``shadowJarExecutable`` gradle task:
See [build_k2dart.bat](./build_k2dart.bat).

Then ``cd`` to the [k2dart-executables](./k2dart-executables) directory
and run the [fetch_k2dart_executable.bat](./k2dart-executables/fetch_k2dart_executable.bat) script that
will copy the jar file to this directory and rename it ``k2dart.jar``

## How to write a new rule to convert some not yet supported Kotlin syntax
- First make sure you have built k2dart as described in the previous section.
- Then you should create a new kotlin code file that contains the syntax that you would like to add support for in the transpiler.
  You should put it in the [k2dart-executables/test_code](./k2dart-executables/test_code) directory. See the other files in this directory
  for examples.
- Next run ``k2dart`` to just output the Abstract Syntax Tree (AST) for the code you have written.
  ``` java -jar .\k2dart.jar --print-ast  "test_code\<filename>.kts" >.\ast\<filename>_ast.txt```
- Once you have the AST of the code with syntax you want to support, you can study to understand how
 to write a new k2dart rule to support it.
- For writing a new k2dart rule, it is best to start by copying an existing rule in the [rules directory](./ktlint-ruleset-k2dart/src/main/kotlin/com/beyondeye/k2dart/rules)
in the [ktlint-ruleset-k2dart](./ktlint-ruleset-k2dart) module, and add it in the same directory. You also have to add the new rule
to the ``k2dart`` [ruleset file](./ktlint-ruleset-k2dart/src/main/kotlin/com/beyondeye/k2dart/CustomRuleSetProvider.kt) in ``getRuleProviders()`` method. Note that unlike in the original ``ktlint`` each rule
is associated with a priority that determine the execution order of the rule (higher priority: rule execute earlier)

 ## Contributing
Writing additional rules to handle transpiling of code that is not currently supported are welcome.

Writing rules is not very difficult. Just take a look at the existing rules to have an idea of how 
it works, or open a issue with what you want to do, and we will try to help with adding the new rules

Thanks to the modular architecture of ktlint, it is also very easy to define different ruleset
to use in different scenarios.


